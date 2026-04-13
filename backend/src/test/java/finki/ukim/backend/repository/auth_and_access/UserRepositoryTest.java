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

    @Test
    void testFindAllWithIdUsernameAndEmail() {
        List<UserWithIdUsernameAndEmail> results = userRepository.findAllWithIdUsernameAndEmail();

        assertThat(results).hasSize(4);

        assertThat(results)
                .extracting(UserWithIdUsernameAndEmail::getUsername,
                        UserWithIdUsernameAndEmail::getEmail)
                .containsExactlyInAnyOrder(
                        tuple("User1", "email1@gmail.com"),
                        tuple("User2", "email2@gmail.com"),
                        tuple("User3", "email3@gmail.com"),
                        tuple("User4", "email4@gmail.com")
                );
    }
}