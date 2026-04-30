package finki.ukim.backend.auth_and_access.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.PasswordResetToken;
import finki.ukim.backend.auth_and_access.model.domain.User;

import java.util.List;
import java.util.Optional;

public interface PasswordResetService {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findActiveByUserId(Long userId);

    void requestPasswordReset(String email);

    User resetPassword(String token, String newPassword, String confirmPassword);

    void deleteInactiveTokens();
}
