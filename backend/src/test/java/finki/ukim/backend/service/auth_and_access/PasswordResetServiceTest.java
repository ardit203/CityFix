package finki.ukim.backend.service.auth_and_access;

import finki.ukim.backend.auth_and_access.helper.PasswordResetHelper;
import finki.ukim.backend.auth_and_access.model.domain.PasswordResetToken;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.exception.InvalidTokenException;
import finki.ukim.backend.auth_and_access.model.exception.TokenNotFoundException;
import finki.ukim.backend.auth_and_access.repository.PasswordResetTokenRepository;
import finki.ukim.backend.auth_and_access.service.domain.PasswordService;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import finki.ukim.backend.auth_and_access.service.domain.impl.PasswordResetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTest {

    @Mock private PasswordResetTokenRepository tokenRepository;
    @Mock private PasswordResetHelper           passwordResetHelper;
    @Mock private UserService                   userService;
    @Mock private PasswordService               passwordService;

    private PasswordResetServiceImpl resetService;

    private User user;

    @BeforeEach
    void setUp() {
        resetService = new PasswordResetServiceImpl(tokenRepository, passwordResetHelper, userService, passwordService);
        user = new User("alice", "$enc", "alice@example.com", Role.ROLE_CITIZEN);
    }

    // ── requestPasswordReset ──────────────────────────────────────────────

    @Test
    void requestPasswordReset_shouldGenerateAndSaveToken_whenEmailExists() {
        when(userService.findByEmail("alice@example.com")).thenReturn(Optional.of(user));
        when(passwordResetHelper.generateToken()).thenReturn("rawToken");
        when(passwordResetHelper.hashToken("rawToken")).thenReturn("hashedToken");

        resetService.requestPasswordReset("alice@example.com");

        verify(tokenRepository).invalidateAllActiveByUserId(any());
        verify(tokenRepository).save(argThat(t -> "hashedToken".equals(t.getTokenHash())));
    }

    @Test
    void requestPasswordReset_shouldDoNothing_whenEmailDoesNotExist() {
        when(userService.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        resetService.requestPasswordReset("ghost@example.com");

        verify(tokenRepository, never()).invalidateAllActiveByUserId(any());
        verify(tokenRepository, never()).save(any());
    }

    // ── resetPassword ─────────────────────────────────────────────────────

    @Test
    void resetPassword_shouldUpdatePasswordAndMarkTokenUsed_whenTokenIsActive() {
        PasswordResetToken activeToken = new PasswordResetToken(user, "hashedToken", 5);
        // token is fresh (expiresAt is 5 min from now), usedAt=null, invalidatedAt=null

        when(passwordResetHelper.hashToken("rawToken")).thenReturn("hashedToken");
        when(tokenRepository.findByTokenHashWithUser("hashedToken")).thenReturn(Optional.of(activeToken));
        when(passwordService.preparePasswordForRegistration("NewPass11!", "NewPass11!")).thenReturn("$newEnc");
        when(userService.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<User> result = resetService.resetPassword("rawToken", "NewPass11!", "NewPass11!");

        assertThat(result).isPresent();
        assertThat(result.get().getPassword()).isEqualTo("$newEnc");
        assertThat(activeToken.isUsed()).isTrue();
        verify(tokenRepository).invalidateOtherActiveByUserId(any(), any());
    }

    @Test
    void resetPassword_shouldThrow_whenTokenHashNotFound() {
        when(passwordResetHelper.hashToken("unknownToken")).thenReturn("unknownHash");
        when(tokenRepository.findByTokenHashWithUser("unknownHash")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resetService.resetPassword("unknownToken", "NewPass11!", "NewPass11!"))
                .isInstanceOf(TokenNotFoundException.class);
    }

    @Test
    void resetPassword_shouldThrow_whenTokenIsExpired() {
        PasswordResetToken expiredToken = new PasswordResetToken();
        expiredToken.setUser(user);
        expiredToken.setTokenHash("expiredHash");
        expiredToken.setExpiresAt(LocalDateTime.now().minusMinutes(1)); // already expired

        when(passwordResetHelper.hashToken("expiredToken")).thenReturn("expiredHash");
        when(tokenRepository.findByTokenHashWithUser("expiredHash")).thenReturn(Optional.of(expiredToken));

        assertThatThrownBy(() -> resetService.resetPassword("expiredToken", "NewPass11!", "NewPass11!"))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void resetPassword_shouldThrow_whenTokenIsAlreadyUsed() {
        PasswordResetToken usedToken = new PasswordResetToken();
        usedToken.setUser(user);
        usedToken.setTokenHash("usedHash");
        usedToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        usedToken.setUsedAt(LocalDateTime.now().minusMinutes(1));

        when(passwordResetHelper.hashToken("usedToken")).thenReturn("usedHash");
        when(tokenRepository.findByTokenHashWithUser("usedHash")).thenReturn(Optional.of(usedToken));

        assertThatThrownBy(() -> resetService.resetPassword("usedToken", "NewPass11!", "NewPass11!"))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void resetPassword_shouldThrow_whenTokenIsInvalidated() {
        PasswordResetToken invalidatedToken = new PasswordResetToken();
        invalidatedToken.setUser(user);
        invalidatedToken.setTokenHash("invalidHash");
        invalidatedToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        invalidatedToken.setInvalidatedAt(LocalDateTime.now().minusMinutes(1));

        when(passwordResetHelper.hashToken("invalidToken")).thenReturn("invalidHash");
        when(tokenRepository.findByTokenHashWithUser("invalidHash")).thenReturn(Optional.of(invalidatedToken));

        assertThatThrownBy(() -> resetService.resetPassword("invalidToken", "NewPass11!", "NewPass11!"))
                .isInstanceOf(InvalidTokenException.class);
    }

    // ── deleteInactiveTokens ──────────────────────────────────────────────

    @Test
    void deleteInactiveTokens_shouldDeleteAllInactiveTokens() {
        PasswordResetToken t1 = new PasswordResetToken();
        PasswordResetToken t2 = new PasswordResetToken();
        List<PasswordResetToken> inactive = List.of(t1, t2);

        when(tokenRepository.findAllInactiveTokens()).thenReturn(inactive);

        resetService.deleteInactiveTokens();

        verify(tokenRepository).deleteAll(inactive);
    }
}
