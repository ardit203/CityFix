package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.PasswordResetToken;

import java.time.LocalDateTime;

public record DisplayPasswordResetTokenDto(
        Long id,
        Long userId,
        String tokenHash,
        LocalDateTime expiresAt,
        LocalDateTime usedAt,
        LocalDateTime invalidatedAt
) {
    public static DisplayPasswordResetTokenDto from(PasswordResetToken passwordResetToken) {
        return new DisplayPasswordResetTokenDto(
                passwordResetToken.getId(),
                passwordResetToken.getUser().getId(),
                passwordResetToken.getTokenHash(),
                passwordResetToken.getExpiresAt(),
                passwordResetToken.getUsedAt(),
                passwordResetToken.getInvalidatedAt()
        );
    }
}
