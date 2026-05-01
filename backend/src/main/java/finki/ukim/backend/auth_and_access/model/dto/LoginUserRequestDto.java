package finki.ukim.backend.auth_and_access.model.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginUserRequestDto(
        @NotBlank(message = "Username is required")
        String username,
        
        @NotBlank(message = "Password is required")
        String password
) {
}