package finki.ukim.backend.auth_and_access.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.PasswordResetToken;
import finki.ukim.backend.auth_and_access.model.domain.User;

import java.util.List;
import java.util.Optional;

public interface PasswordResetService {
    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findActiveByUserId(Long userId);

    void requestPasswordReset(String email);

    Optional<User> resetPassword(String token, String newPassword, String confirmPassword);

    void deleteInactiveTokens();
}
