package finki.ukim.backend.repository.auth_and_access;


import finki.ukim.backend.auth_and_access.model.domain.PasswordResetToken;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.repository.PasswordResetTokenRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PasswordResetTokenRepositoryTest {

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
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private User user1;
    private User user2;

    private PasswordResetToken activeTokenUser1;
    private PasswordResetToken expiredTokenUser1;
    private PasswordResetToken usedTokenUser1;
    private PasswordResetToken invalidatedTokenUser2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(
                new User("user1", "password1", "user1@gmail.com", Role.ROLE_CITIZEN)
        );

        user2 = userRepository.save(
                new User("user2", "password2", "user2@gmail.com", Role.ROLE_MANAGER)
        );

        activeTokenUser1 = saveToken(
                user1,
                "hash-active-u1",
                LocalDateTime.now().plusHours(2),
                null,
                null
        );

        expiredTokenUser1 = saveToken(
                user1,
                "hash-expired-u1",
                LocalDateTime.now().minusHours(2),
                null,
                null
        );

        usedTokenUser1 = saveToken(
                user1,
                "hash-used-u1",
                LocalDateTime.now().plusHours(2),
                LocalDateTime.now().minusMinutes(10),
                null
        );

        invalidatedTokenUser2 = saveToken(
                user2,
                "hash-invalidated-u2",
                LocalDateTime.now().plusHours(2),
                null,
                LocalDateTime.now().minusMinutes(5)
        );

        entityManager.flush();
        entityManager.clear();
    }

    private PasswordResetToken saveToken(
            User user,
            String tokenHash,
            LocalDateTime expiresAt,
            LocalDateTime usedAt,
            LocalDateTime invalidatedAt
    ) {
        PasswordResetToken token = new PasswordResetToken();

        // Adapt these setters if your entity uses constructor/builder instead
        token.setUser(user);
        token.setTokenHash(tokenHash);
        token.setExpiresAt(expiresAt);
        token.setUsedAt(usedAt);
        token.setInvalidatedAt(invalidatedAt);

        return passwordResetTokenRepository.save(token);
    }

    @Test
    void findByTokenHash_shouldReturnMatchingToken() {
        Optional<PasswordResetToken> result =
                passwordResetTokenRepository.findByTokenHash("hash-active-u1");

        assertThat(result).isPresent();
        assertThat(result.get().getTokenHash()).isEqualTo("hash-active-u1");
        assertThat(result.get().getUser().getId()).isEqualTo(user1.getId());
    }

    @Test
    void findAllByUser_Id_shouldReturnAllTokensForUser() {
        List<PasswordResetToken> result =
                passwordResetTokenRepository.findAllByUser_Id(user1.getId());

        assertThat(result).hasSize(3);
        assertThat(result)
                .extracting(PasswordResetToken::getTokenHash)
                .containsExactlyInAnyOrder(
                        "hash-active-u1",
                        "hash-expired-u1",
                        "hash-used-u1"
                );
    }

    @Test
    void findByUserId_shouldReturnAllTokensForUser_andFetchUserGraph() {
        List<PasswordResetToken> result =
                passwordResetTokenRepository.findByUserId(user1.getId());

        PersistenceUnitUtil util = entityManagerFactory.getPersistenceUnitUtil();

        assertThat(result).hasSize(3);
        assertThat(result).allSatisfy(token -> {
            assertThat(util.isLoaded(token, "user")).isTrue();
            assertThat(token.getUser()).isNotNull();
            assertThat(token.getUser().getId()).isEqualTo(user1.getId());
        });
    }

    @Test
    void findActiveByUserId_shouldReturnOnlyActiveToken() {
        Optional<PasswordResetToken> result =
                passwordResetTokenRepository.findActiveByUserId(user1.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getTokenHash()).isEqualTo("hash-active-u1");
        assertThat(result.get().getExpiresAt()).isAfter(LocalDateTime.now());
        assertThat(result.get().getUsedAt()).isNull();
        assertThat(result.get().getInvalidatedAt()).isNull();
    }

    @Test
    void findActiveByUserId_shouldReturnEmptyWhenUserHasNoActiveToken() {
        Optional<PasswordResetToken> result =
                passwordResetTokenRepository.findActiveByUserId(user2.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void findAllInactiveTokens_shouldReturnExpiredUsedAndInvalidatedTokens() {
        List<PasswordResetToken> result =
                passwordResetTokenRepository.findAllInactiveTokens();

        assertThat(result).hasSize(3);
        assertThat(result)
                .extracting(PasswordResetToken::getTokenHash)
                .containsExactlyInAnyOrder(
                        "hash-expired-u1",
                        "hash-used-u1",
                        "hash-invalidated-u2"
                );
    }
}