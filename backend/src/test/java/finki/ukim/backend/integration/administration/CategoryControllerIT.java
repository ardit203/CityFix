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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class CategoryControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("cityfix_cat_it")
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
    private Long   deptId;

    @BeforeEach
    void setUp() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        authToken = obtainToken("cat_admin_" + uniqueSuffix, "CatPass11!", "cat_admin_" + uniqueSuffix + "@example.com", Role.ROLE_ADMINISTRATOR);
        deptId    = createDepartment("Roads_CatIT_" + uniqueSuffix);
    }

    private String obtainToken(String username, String password, String email, Role role) throws Exception {
        Map<String, Object> profile = Map.of(
                "name", "Admin", "surname", "X",
                "gender", "MALE", "dateOfBirth", LocalDate.of(1990, 1, 1).toString(),
                "phoneNumber", "0");
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Map.of(
                        "username", username, "password", password, "confirmPassword", password,
                        "email", email, "role", role.name(), "userProfile", profile))))
                .andExpect(status().isOk());

        MvcResult loginResult = mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("username", username, "password", password))))
                .andExpect(status().isOk())
                .andReturn();
        return (String) mapper.readValue(loginResult.getResponse().getContentAsString(), Map.class).get("token");
    }

    private Long createDepartment(String name) throws Exception {
        MvcResult r = mockMvc.perform(post("/api/department")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("name", name, "description", "desc"))))
                .andExpect(status().isOk())
                .andReturn();
        return ((Number) mapper.readValue(r.getResponse().getContentAsString(), Map.class).get("id")).longValue();
    }

    private Long createCategory(String name, Long departmentId) throws Exception {
        Map<String, Object> body = Map.of("name", name, "description", "desc", "departmentId", departmentId);
        MvcResult r = mockMvc.perform(post("/api/category")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn();
        return ((Number) mapper.readValue(r.getResponse().getContentAsString(), Map.class).get("id")).longValue();
    }

    // ── GET /api/category ─────────────────────────────────────────────────

    @Test
    void findAll_shouldReturn200WithList() throws Exception {
        mockMvc.perform(get("/api/category")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ── POST /api/category ────────────────────────────────────────────────

    @Test
    void create_shouldReturn200WithCreatedCategory() throws Exception {
        Map<String, Object> body = Map.of("name", "Pothole_IT", "description", "potholes", "departmentId", deptId);

        mockMvc.perform(post("/api/category")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pothole_IT"))
                .andExpect(jsonPath("$.department.id").value(deptId));
    }

    // ── GET /api/category/{id} ────────────────────────────────────────────

    @Test
    void findById_shouldReturn200_whenCategoryExists() throws Exception {
        Long catId = createCategory("Cat_FindById", deptId);

        mockMvc.perform(get("/api/category/" + catId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(catId));
    }

    @Test
    void findById_shouldReturn404_whenCategoryDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/category/999999")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    // ── GET /api/category/name/{name} ─────────────────────────────────────

    @Test
    void findByName_shouldReturn200_whenNameExists() throws Exception {
        createCategory("UniqueNameCat", deptId);

        mockMvc.perform(get("/api/category/name/UniqueNameCat")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UniqueNameCat"));
    }

    // ── GET /api/category/department/{departmentId} ───────────────────────

    @Test
    void findByDepartmentId_shouldReturn200WithList() throws Exception {
        createCategory("RoadCat1", deptId);
        createCategory("RoadCat2", deptId);

        mockMvc.perform(get("/api/category/department/" + deptId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ── PUT /api/category/{id} ────────────────────────────────────────────

    @Test
    void update_shouldReturn200WithUpdatedCategory() throws Exception {
        Long catId = createCategory("Cat_Update", deptId);
        Map<String, Object> body = Map.of("name", "Cat_Updated", "description", "new desc", "departmentId", deptId);

        mockMvc.perform(put("/api/category/" + catId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cat_Updated"));
    }

    // ── DELETE /api/category/{id} ─────────────────────────────────────────

    @Test
    void delete_shouldReturn200_whenCategoryExists() throws Exception {
        Long catId = createCategory("Cat_Delete", deptId);

        mockMvc.perform(delete("/api/category/" + catId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(catId));
    }

    @Test
    void delete_shouldReturn404_whenCategoryDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/category/777777")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }
}
