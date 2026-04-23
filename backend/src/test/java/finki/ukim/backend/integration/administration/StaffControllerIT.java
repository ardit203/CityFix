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
public class StaffControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("cityfix_staff_it")
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

    private String adminToken;
    private Long   deptId;
    private Long   munId;
    private String employeeUsername;

    @BeforeEach
    void setUp() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        adminToken = registerAndGetToken("staff_admin_" + uniqueSuffix,    "StaffPass11!", "staff_admin_" + uniqueSuffix + "@example.com",    Role.ROLE_ADMINISTRATOR);
        deptId     = createDepartment("Staff_Dept_" + uniqueSuffix);
        munId      = createMunicipality("Staff_Mun_" + uniqueSuffix, "STF" + uniqueSuffix);
        employeeUsername = "emp_for_staff_" + System.nanoTime();
        registerAndGetToken(employeeUsername, "EmpPass11!", employeeUsername + "@example.com", Role.ROLE_EMPLOYEE);
    }

    private String registerAndGetToken(String username, String password, String email, Role role) throws Exception {
        Map<String, Object> profile = Map.of(
                "name", "Test", "surname", "User",
                "gender", "MALE", "dateOfBirth", LocalDate.of(1990, 1, 1).toString(),
                "phoneNumber", "123");
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
        Map<?, ?> resp = mapper.readValue(loginResult.getResponse().getContentAsString(), Map.class);
        return (String) resp.get("token");
    }

    private Long createDepartment(String name) throws Exception {
        MvcResult r = mockMvc.perform(post("/api/department")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("name", name + "_" + System.nanoTime(), "description", "d"))))
                .andExpect(status().isOk())
                .andReturn();
        return ((Number) mapper.readValue(r.getResponse().getContentAsString(), Map.class).get("id")).longValue();
    }

    private Long createMunicipality(String name, String code) throws Exception {
        MvcResult r = mockMvc.perform(post("/api/municipality")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of(
                                "name", name + "_" + System.nanoTime(),
                                "code", code + System.nanoTime()))))
                .andExpect(status().isOk())
                .andReturn();
        return ((Number) mapper.readValue(r.getResponse().getContentAsString(), Map.class).get("id")).longValue();
    }

    private Long createStaff(String username, Long departmentId, Long municipalityId) throws Exception {
        Map<String, Object> body = Map.of(
                "username", username, "departmentId", departmentId, "municipalityId", municipalityId);
        MvcResult r = mockMvc.perform(post("/api/staff")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn();
        return ((Number) mapper.readValue(r.getResponse().getContentAsString(), Map.class).get("id")).longValue();
    }

    // ── GET /api/staff ────────────────────────────────────────────────────

    @Test
    void findAll_shouldReturn200WithList() throws Exception {
        mockMvc.perform(get("/api/staff")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findAll_shouldReturn401_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/staff"))
                .andExpect(status().isUnauthorized());
    }

    // ── POST /api/staff ───────────────────────────────────────────────────

    @Test
    void create_shouldReturn200WithCreatedStaff_whenUserIsEmployee() throws Exception {
        Long staffId = createStaff(employeeUsername, deptId, munId);

        assertThat(staffId).isNotNull().isPositive();
    }

    @Test
    void create_shouldReturnConflictOrBadRequest_whenUserIsCitizen() throws Exception {
        String citizenUsername = "citizen_staff_" + System.nanoTime();
        registerAndGetToken(citizenUsername, "CitPass11!", citizenUsername + "@example.com", Role.ROLE_CITIZEN);

        Map<String, Object> body = Map.of(
                "username", citizenUsername, "departmentId", deptId, "municipalityId", munId);

        mockMvc.perform(post("/api/staff")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isConflict());
    }

    // ── GET /api/staff/{id} ───────────────────────────────────────────────

    @Test
    void findById_shouldReturn200_whenStaffExists() throws Exception {
        Long staffId = createStaff(employeeUsername, deptId, munId);

        mockMvc.perform(get("/api/staff/" + staffId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(staffId));
    }

    @Test
    void findById_shouldReturn404_whenStaffDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/staff/999999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    // ── GET /api/staff/username/{username} ────────────────────────────────

    @Test
    void findByUsername_shouldReturn200_whenStaffExists() throws Exception {
        createStaff(employeeUsername, deptId, munId);

        mockMvc.perform(get("/api/staff/username/" + employeeUsername)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value(employeeUsername));
    }

    // ── GET /api/staff/department/{departmentId} ──────────────────────────

    @Test
    void findByDepartmentId_shouldReturn200WithList() throws Exception {
        createStaff(employeeUsername, deptId, munId);

        mockMvc.perform(get("/api/staff/department/" + deptId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ── GET /api/staff/municipality/{municipalityId} ──────────────────────

    @Test
    void findByMunicipalityId_shouldReturn200WithList() throws Exception {
        createStaff(employeeUsername, deptId, munId);

        mockMvc.perform(get("/api/staff/municipality/" + munId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ── DELETE /api/staff/{id} ────────────────────────────────────────────

    @Test
    void delete_shouldReturn200_whenStaffExists() throws Exception {
        Long staffId = createStaff(employeeUsername, deptId, munId);

        mockMvc.perform(delete("/api/staff/" + staffId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(staffId));
    }

    @Test
    void delete_shouldReturn404_whenStaffDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/staff/888888")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }
}
