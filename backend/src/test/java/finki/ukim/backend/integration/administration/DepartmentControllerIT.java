package finki.ukim.backend.integration.administration;

import com.fasterxml.jackson.databind.ObjectMapper;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class DepartmentControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("cityfix_dept_it")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired MockMvc     mockMvc;
    @Autowired ObjectMapper mapper;

    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        authToken = obtainToken("dept_admin_" + uniqueSuffix, "DeptPass11!", "dept_admin_" + uniqueSuffix + "@example.com", Role.ROLE_ADMINISTRATOR);
    }

    /** Helper to register + login and extract JWT. */
    private String obtainToken(String username, String password, String email, Role role) throws Exception {
        Map<String, Object> profile = Map.of(
                "name", "Admin", "surname", "User",
                "gender", "MALE", "dateOfBirth", LocalDate.of(1990, 1, 1).toString(),
                "phoneNumber", "0000");
        Map<String, Object> regBody = Map.of(
                "username", username, "password", password, "confirmPassword", password,
                "email", email, "role", role.name(), "userProfile", profile);

        // Register
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(regBody)))
                .andExpect(status().isOk());

        MvcResult loginResult = mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("username", username, "password", password))))
                .andExpect(status().isOk())
                .andReturn();

        Map<?, ?> response = mapper.readValue(loginResult.getResponse().getContentAsString(), Map.class);
        return (String) response.get("token");
    }

    // ── GET /api/department ───────────────────────────────────────────────

    @Test
    void findAll_shouldReturn200WithList_whenAuthenticated() throws Exception {
        mockMvc.perform(get("/api/department")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findAll_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/department"))
                .andExpect(status().isUnauthorized());
    }

    // ── POST /api/department ──────────────────────────────────────────────

    @Test
    void create_shouldReturn200WithCreatedDepartment_whenAuthenticated() throws Exception {
        Map<String, String> body = Map.of("name", "Roads_IT", "description", "Road maintenance");

        mockMvc.perform(post("/api/department")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Roads_IT"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void create_shouldReturn401_whenNotAuthenticated() throws Exception {
        Map<String, String> body = Map.of("name", "Unauth_Dept", "description", "desc");

        mockMvc.perform(post("/api/department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
    }

    // ── GET /api/department/{id} ──────────────────────────────────────────

    @Test
    void findById_shouldReturn200_whenDepartmentExists() throws Exception {
        // Create a department first
        MvcResult createResult = mockMvc.perform(post("/api/department")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("name", "Dept_GetById_" + UUID.randomUUID().toString().substring(0, 8), "description", "d"))))
                .andExpect(status().isOk())
                .andReturn();
        Map<?, ?> created = mapper.readValue(createResult.getResponse().getContentAsString(), Map.class);
        Long id = ((Number) created.get("id")).longValue();

        mockMvc.perform(get("/api/department/" + id)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void findById_shouldReturn404_whenDepartmentDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/department/999999")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    // ── PUT /api/department/{id} ──────────────────────────────────────────

    @Test
    void update_shouldReturn200WithUpdatedDepartment_whenExists() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/department")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("name", "Dept_Update_" + UUID.randomUUID().toString().substring(0, 8), "description", "old"))))
                .andExpect(status().isOk())
                .andReturn();
        Map<?, ?> created = mapper.readValue(createResult.getResponse().getContentAsString(), Map.class);
        Long id = ((Number) created.get("id")).longValue();

        mockMvc.perform(put("/api/department/" + id)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("name", "Dept_Updated", "description", "new"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dept_Updated"));
    }

    // ── DELETE /api/department/{id} ───────────────────────────────────────

    @Test
    void delete_shouldReturn200_whenDepartmentExists() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/department")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("name", "Dept_Delete_" + UUID.randomUUID().toString().substring(0, 8), "description", "del"))))
                .andExpect(status().isOk())
                .andReturn();
        Map<?, ?> created = mapper.readValue(createResult.getResponse().getContentAsString(), Map.class);
        Long id = ((Number) created.get("id")).longValue();

        mockMvc.perform(delete("/api/department/" + id)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void delete_shouldReturn404_whenDepartmentDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/department/888888")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }
}
