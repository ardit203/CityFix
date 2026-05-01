package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Gender;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateMyProfileDto(
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
    public UserProfile toUserProfile() {
        return new UserProfile(
                name,
                surname,
                address.toAddress(),
                dateOfBirth,
                gender,
                phoneNumber
        );
    }
}