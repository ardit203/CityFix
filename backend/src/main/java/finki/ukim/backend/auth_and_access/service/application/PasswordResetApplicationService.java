package finki.ukim.backend.auth_and_access.service.application;

import finki.ukim.backend.auth_and_access.model.dto.DisplayBasicUserDto;
import finki.ukim.backend.auth_and_access.model.dto.DisplayPasswordResetTokenDto;

import java.util.Optional;

public interface PasswordResetApplicationService {
    Optional<DisplayPasswordResetTokenDto> findByToken(String token);

    Optional<DisplayPasswordResetTokenDto> findActiveByUserId(Long userId);

    void requestPasswordReset(String email);

    Optional<DisplayBasicUserDto> resetPassword(String token, String newPassword, String confirmPassword);

    void deleteInactiveTokens();
}
