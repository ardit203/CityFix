package finki.ukim.backend.auth_and_access.service.application.impl;

import finki.ukim.backend.auth_and_access.model.dto.DisplayBasicUserDto;
import finki.ukim.backend.auth_and_access.model.dto.DisplayPasswordResetTokenDto;
import finki.ukim.backend.auth_and_access.service.application.PasswordResetApplicationService;
import finki.ukim.backend.auth_and_access.service.domain.PasswordResetService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PasswordResetApplicationServiceImpl implements PasswordResetApplicationService {
    private final PasswordResetService passwordResetService;

    @Override
    public Optional<DisplayPasswordResetTokenDto> findByToken(String token) {
        return passwordResetService
                .findByToken(token)
                .map(DisplayPasswordResetTokenDto::from);
    }

    @Override
    public Optional<DisplayPasswordResetTokenDto> findActiveByUserId(Long userId) {
        return passwordResetService
                .findActiveByUserId(userId)
                .map(DisplayPasswordResetTokenDto::from);
    }

    @Override
    public String requestPasswordReset(String email) {
        return passwordResetService.requestPasswordReset(email);
    }

    @Override
    public Optional<DisplayBasicUserDto> resetPassword(String token, String newPassword, String confirmPassword) {
        return passwordResetService
                .resetPassword(token, newPassword, confirmPassword)
                .map(DisplayBasicUserDto::from);
    }

    @Override
    public void deleteInactiveTokens() {
        passwordResetService.deleteInactiveTokens();
    }
}
