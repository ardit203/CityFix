package finki.ukim.backend.auth_and_access.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordDto(
        @NotBlank(message = "Token is required")
        String token,
        
        @NotBlank(message = "New password is required")
        String newPassword,
        
        @NotBlank(message = "Confirm password is required")
        String confirmPassword
) {
}
