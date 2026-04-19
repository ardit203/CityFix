package finki.ukim.backend.auth_and_access.model.dto;

public record ResetPasswordDto(
        String token,
        String newPassword,
        String confirmPassword
) {
}
