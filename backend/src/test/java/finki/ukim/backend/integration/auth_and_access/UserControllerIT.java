package finki.ukim.backend.integration.auth_and_access;

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
public class UserControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("cityfix_it")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired MockMvc    mockMvc;
    @Autowired ObjectMapper mapper;

    private static final String REGISTER_URL      = "/api/user/register";
    private static final String LOGIN_URL          = "/api/user/login";
    private static final String FIND_BY_USERNAME   = "/api/user/username/";
    private static final String ME_URL             = "/api/user/me";
    private static final String UPDATE_URL         = "/api/user/update/";
    private static final String DELETE_URL         = "/api/user/delete/";

    /** Helper – register a user and return its JWT. */
    private String registerAndLogin(String username, String password, String email, Role role) throws Exception {
        Map<String, Object> profile = Map.of(
                "name", "Test",
                "surname", "User",
                "gender", "MALE",
                "dateOfBirth", LocalDate.of(1995, 1, 1).toString(),
                "phoneNumber", "1234567890"
        );
        Map<String, Object> regBody = Map.of(
                "username", username,
                "password", password,
                "confirmPassword", password,
                "email", email,
                "role", role.name(),
                "userProfile", profile
        );
        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(regBody)))
                .andExpect(status().isOk());

        MvcResult loginResult = mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("username", username, "password", password))))
                .andExpect(status().isOk())
                .andReturn();

        Map<?, ?> loginResponse = mapper.readValue(loginResult.getResponse().getContentAsString(), Map.class);
        return (String) loginResponse.get("token");
    }

    @BeforeEach
    void setUp() {
        // Each test has its own Spring context state; for repeated runs we rely on @DynamicPropertySource
        // and Spring Boot's create-drop DDL to get a clean schema.
    }

    // ── POST /api/user/register ───────────────────────────────────────────

    @Test
    void register_shouldReturn200_whenDataIsValid() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String username = "alice_" + uniqueSuffix;
        String email = "alice_" + uniqueSuffix + "@example.com";
        Map<String, Object> profile = Map.of(
                "name", "Alice", "surname", "Smith",
                "gender", "FEMALE", "dateOfBirth", "1990-05-15", "phoneNumber", "111222333");
        Map<String, Object> body = Map.of(
                "username", username,
                "password", "TestPass11!",
                "confirmPassword", "TestPass11!",
                "email", email,
                "role", Role.ROLE_CITIZEN.name(),
                "userProfile", profile);

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void register_shouldReturnConflict_whenUsernameAlreadyExists() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String username = "dup_" + uniqueSuffix;
        
        // First registration
        Map<String, Object> profile = Map.of(
                "name", "Bob", "surname", "Jones",
                "gender", "MALE", "dateOfBirth", "1985-03-10", "phoneNumber", "999");
        Map<String, Object> body = Map.of(
                "username", username,
                "password", "TestPass11!",
                "confirmPassword", "TestPass11!",
                "email", username + "_1@example.com",
                "role", Role.ROLE_CITIZEN.name(),
                "userProfile", profile);

        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body))).andExpect(status().isOk());

        // Second registration with same username
        Map<String, Object> body2 = Map.of(
                "username", username,
                "password", "TestPass11!",
                "confirmPassword", "TestPass11!",
                "email", username + "_2@example.com",
                "role", Role.ROLE_CITIZEN.name(),
                "userProfile", profile);

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body2)))
                .andExpect(status().isConflict());
    }

    // ── POST /api/user/login ──────────────────────────────────────────────

    @Test
    void login_shouldReturn200WithToken_whenCredentialsAreCorrect() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        registerAndLogin("login_" + uniqueSuffix, "TestPass11!", "login_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);
        // If we got here without exception the login succeeded and returned a token
    }

    @Test
    void login_shouldReturn400_whenPasswordIsWrong() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String username = "wrong_" + uniqueSuffix;
        Map<String, Object> profile = Map.of(
                "name", "X", "surname", "Y",
                "gender", "MALE", "dateOfBirth", "2000-01-01", "phoneNumber", "0");
        Map<String, Object> regBody = Map.of(
                "username", username,
                "password", "TestPass11!",
                "confirmPassword", "TestPass11!",
                "email", username + "@example.com",
                "role", Role.ROLE_CITIZEN.name(),
                "userProfile", profile);
        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(regBody))).andExpect(status().isOk());

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("username", username, "password", "WrongPassword1!"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturn400_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(Map.of("username", "nobody_" + UUID.randomUUID(), "password", "pass"))))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/user/username/{username} ─────────────────────────────────

    @Test
    void findByUsername_shouldReturn200_whenUserExists() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String username = "findme_" + uniqueSuffix;
        String token = registerAndLogin(username, "TestPass11!", username + "@example.com", Role.ROLE_CITIZEN);

        mockMvc.perform(get(FIND_BY_USERNAME + username)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    void findByUsername_shouldReturn404_whenUserDoesNotExist() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String token = registerAndLogin("t4_" + uniqueSuffix, "TestPass11!", "t4_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);

        mockMvc.perform(get(FIND_BY_USERNAME + "nonexistent_" + uniqueSuffix)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    // ── GET /api/user/me ──────────────────────────────────────────────────

    @Test
    void me_shouldReturn200WithAuthenticatedUser_whenTokenIsValid() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String username = "me_" + uniqueSuffix;
        String token = registerAndLogin(username, "TestPass11!", username + "@example.com", Role.ROLE_CITIZEN);

        mockMvc.perform(get(ME_URL).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    void me_shouldReturn401_whenNoTokenProvided() throws Exception {
        mockMvc.perform(get(ME_URL))
                .andExpect(status().isUnauthorized());
    }

    // ── DELETE /api/user/delete/{id} ──────────────────────────────────────

    @Test
    void delete_shouldReturn400_whenUserIdDoesNotExist() throws Exception {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String token = registerAndLogin("del_" + uniqueSuffix, "TestPass11!", "del_" + uniqueSuffix + "@example.com", Role.ROLE_CITIZEN);

        mockMvc.perform(delete(DELETE_URL + 999999L)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }
}
