package finki.ukim.backend.repository.auth_and_access;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.repository.UserProfileRepository;
import finki.ukim.backend.auth_and_access.repository.UserRepository;
import finki.ukim.backend.common.config.JpaConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnitUtil;
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

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private Long user1Id;
    private Long user2Id;
    private Long profile1Id;
    private Long profile2Id;

    @BeforeEach
    void setUp() {
        User user1 = userRepository.save(
                new User("User1", "password1", "email1@gmail.com", Role.ROLE_CITIZEN)
        );

        User user2 = userRepository.save(
                new User("User2", "password2", "email2@gmail.com", Role.ROLE_MANAGER)
        );


        UserProfile profile1 = new UserProfile(user1,"Name1", "Surname1");

        UserProfile profile2 = new UserProfile(user2, "Name2", "Surname2");


        profile1 = userProfileRepository.save(profile1);
        profile2 = userProfileRepository.save(profile2);

        user1Id = user1.getId();
        user2Id = user2.getId();
        profile1Id = profile1.getUserId();
        profile2Id = profile2.getUserId();

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findWithLockByUserId_shouldReturnProfileForGivenUserId() {
        var result = userProfileRepository.findWithLockByUserId(user1Id);

        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo(profile1Id);
        assertThat(result.get().getUser().getId()).isEqualTo(user1Id);
    }

    @Test
    void findWithUserByUserId_shouldReturnProfileAndFetchUser() {
        var result = userProfileRepository.findWithUserByUserId(user1Id);

        assertThat(result).isPresent();

        UserProfile profile = result.get();
        PersistenceUnitUtil util = entityManagerFactory.getPersistenceUnitUtil();

        assertThat(profile.getUserId()).isEqualTo(profile1Id);
        assertThat(util.isLoaded(profile, "user")).isTrue();
        assertThat(profile.getUser().getId()).isEqualTo(user1Id);
        assertThat(profile.getUser().getUsername()).isEqualTo("User1");
    }

    @Test
    void findAllWithUser_shouldReturnAllProfilesAndFetchUserForEach() {
        List<UserProfile> results = userProfileRepository.findAllWithUser();

        PersistenceUnitUtil util = entityManagerFactory.getPersistenceUnitUtil();

        assertThat(results).hasSize(2);

        assertThat(results)
                .extracting(UserProfile::getUserId)
                .containsExactlyInAnyOrder(profile1Id, profile2Id);

        assertThat(results).allSatisfy(profile -> {
            assertThat(util.isLoaded(profile, "user")).isTrue();
            assertThat(profile.getUser()).isNotNull();
            assertThat(profile.getUser().getId()).isNotNull();
        });
    }
}