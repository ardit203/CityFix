package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateMyAccountDto(
        @NotBlank(message = "Username is required")
        String username,
        
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,
        
        Boolean notificationsEnabled
) {
    public User toUser() {
        return new User(
                username,
                "",
                email,
                Role.ROLE_CITIZEN,
                notificationsEnabled
        );
    }
}