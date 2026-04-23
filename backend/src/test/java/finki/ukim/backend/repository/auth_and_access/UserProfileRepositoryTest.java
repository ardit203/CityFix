package finki.ukim.backend.repository.auth_and_access;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Gender;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.repository.UserProfileRepository;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserProfileRepositoryTest {

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

    @Autowired
    private UserProfileRepository userProfileRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User("alice", "pw1", "alice@example.com", Role.ROLE_CITIZEN));
        user2 = userRepository.save(new User("bob",   "pw2", "bob@example.com",   Role.ROLE_EMPLOYEE));

        UserProfile profile1 = new UserProfile(user1, "Alice", "Smith", null, LocalDate.of(1990, 1, 1), Gender.FEMALE, "111");
        userProfileRepository.save(profile1);

        // user2 intentionally has no profile
    }

    @Test
    void findByUserId_shouldReturnProfile_whenExists() {
        Optional<UserProfile> result = userProfileRepository.findByUserId(user1.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alice");
        assertThat(result.get().getSurname()).isEqualTo("Smith");
    }

    @Test
    void findByUserId_shouldReturnEmpty_whenUserHasNoProfile() {
        Optional<UserProfile> result = userProfileRepository.findByUserId(user2.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void findByUser_Username_shouldReturnProfile_whenExists() {
        Optional<UserProfile> result = userProfileRepository.findByUser_Username("alice");

        assertThat(result).isPresent();
        assertThat(result.get().getUser().getUsername()).isEqualTo("alice");
    }

    @Test
    void findByUser_Username_shouldReturnEmpty_whenUsernameHasNoProfile() {
        Optional<UserProfile> result = userProfileRepository.findByUser_Username("bob");

        assertThat(result).isEmpty();
    }

    @Test
    void findByUser_Username_shouldReturnEmpty_whenUsernameDoesNotExist() {
        Optional<UserProfile> result = userProfileRepository.findByUser_Username("nobody");

        assertThat(result).isEmpty();
    }
}
