package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Gender;
import finki.ukim.backend.auth_and_access.model.enums.Role;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AdminUpdateUserDto(
        @NotBlank(message = "Username is required")
        String username,
        
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,
        Boolean notificationsEnabled,
        @NotBlank(message = "Name is required")
        String name,
        
        @NotBlank(message = "Surname is required")
        String surname,
        
        @Valid
        AddressDto address,
        
        LocalDate dateOfBirth,
        Gender gender,
        
        @Pattern(regexp = "^\\+?[1-9]\\d{7,14}$", message = "Phone number must be valid and contain 8 to 15 digits, optionally starting with '+'")
        String phoneNumber
) {
    public User toUser() {
        User user = new User(
                username,
                "",
                email,
                Role.ROLE_CITIZEN,
                notificationsEnabled
        );

        UserProfile userProfile = new UserProfile(
                name,
                surname,
                address.toAddress(),
                dateOfBirth,
                gender,
                phoneNumber
        );

        user.setProfile(userProfile);

        return user;
    }
}