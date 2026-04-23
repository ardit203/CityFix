package finki.ukim.backend.repository.auth_and_access;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.projection.UserWithIdUsernameAndEmail;
import finki.ukim.backend.auth_and_access.repository.UserRepository;
import finki.ukim.backend.common.config.JpaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@Testcontainers
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("cityfix_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.saveAll(List.of(
                new User("User1", "password1", "email1@gmail.com", Role.ROLE_CITIZEN),
                new User("User2", "password2", "email2@gmail.com", Role.ROLE_ADMINISTRATOR),
                new User("User3", "password3", "email3@gmail.com", Role.ROLE_EMPLOYEE),
                new User("User4", "password4", "email4@gmail.com", Role.ROLE_MANAGER)
        ));
    }

    // ── findAllWithIdUsernameAndEmail (native query) ───────────────────────

    @Test
    void findAllWithIdUsernameAndEmail_shouldReturnAllUsers() {
        List<UserWithIdUsernameAndEmail> results = userRepository.findAllWithIdUsernameAndEmail();

        assertThat(results).hasSize(4);
        assertThat(results)
                .extracting(UserWithIdUsernameAndEmail::getUsername, UserWithIdUsernameAndEmail::getEmail)
                .containsExactlyInAnyOrder(
                        tuple("User1", "email1@gmail.com"),
                        tuple("User2", "email2@gmail.com"),
                        tuple("User3", "email3@gmail.com"),
                        tuple("User4", "email4@gmail.com")
                );
    }

    // ── findByUsername ────────────────────────────────────────────────────

    @Test
    void findByUsername_shouldReturnUser_whenUsernameExists() {
        Optional<User> result = userRepository.findByUsername("User1");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("email1@gmail.com");
        assertThat(result.get().getRole()).isEqualTo(Role.ROLE_CITIZEN);
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUsernameDoesNotExist() {
        Optional<User> result = userRepository.findByUsername("nobody");

        assertThat(result).isEmpty();
    }

    // ── findByEmail ───────────────────────────────────────────────────────

    @Test
    void findByEmail_shouldReturnUser_whenEmailExists() {
        Optional<User> result = userRepository.findByEmail("email3@gmail.com");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("User3");
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailDoesNotExist() {
        Optional<User> result = userRepository.findByEmail("ghost@example.com");

        assertThat(result).isEmpty();
    }

    // ── existsByUsername / existsByEmail ──────────────────────────────────

    @Test
    void existsByUsername_shouldReturnTrue_whenUsernameExists() {
        assertThat(userRepository.existsByUsername("User2")).isTrue();
    }

    @Test
    void existsByUsername_shouldReturnFalse_whenUsernameDoesNotExist() {
        assertThat(userRepository.existsByUsername("nonexistent")).isFalse();
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenEmailExists() {
        assertThat(userRepository.existsByEmail("email4@gmail.com")).isTrue();
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenEmailDoesNotExist() {
        assertThat(userRepository.existsByEmail("unknown@example.com")).isFalse();
    }

    // ── findAllByRole ─────────────────────────────────────────────────────

    @Test
    void findAllByRole_shouldReturnOnlyUsersWithThatRole() {
        List<User> citizens = userRepository.findAllByRole(Role.ROLE_CITIZEN);

        assertThat(citizens).hasSize(1);
        assertThat(citizens.get(0).getUsername()).isEqualTo("User1");
    }

//    @Test
//    void findAllByRole_shouldReturnEmpty_whenNoUsersWithRole() {
//        // No ROLE_GUEST in the setup data
//        List<User> results = userRepository.findAllByRole(Role.ROLE_GUEST);
//
//        assertThat(results).isEmpty();
//    }
}