package finki.ukim.backend.auth_and_access.model.dto;

public record ChangePasswordDto(
        String currentPassword,
        String newPassword,
        String confirmNewPassword
) {
}
