package finki.ukim.backend.integration.auth_and_access;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class PasswordResetControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("cityfix_pwreset_it")
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

    // ── POST /api/password-reset/request ─────────────────────────────────

    @Test
    void requestPasswordReset_shouldReturn200_forKnownEmail() throws Exception {
        // Endpoint always returns 200 to avoid user enumeration
        mockMvc.perform(post("/api/password-reset/request")
                        .param("email", "any.email@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    void requestPasswordReset_shouldReturn200_forUnknownEmail() throws Exception {
        // Even for unknown emails the endpoint must stay silent (200)
        mockMvc.perform(post("/api/password-reset/request")
                        .param("email", "ghost@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    void requestPasswordReset_shouldReturn400_whenEmailParamIsMissing() throws Exception {
        mockMvc.perform(post("/api/password-reset/request"))
                .andExpect(status().isBadRequest());
    }

    // ── POST /api/password-reset/confirm ─────────────────────────────────

    @Test
    void confirmPasswordReset_shouldReturn400_whenTokenIsInvalidOrNotFound() throws Exception {
        Map<String, Object> body = Map.of(
                "token", "definitely-not-a-valid-token",
                "newPassword", "NewPass11!",
                "confirmPassword", "NewPass11!"
        );

        mockMvc.perform(post("/api/password-reset/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }
}
