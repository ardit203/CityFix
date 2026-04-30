package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Gender;

import java.time.LocalDate;

public record UpdateMyProfileDto(
        String name,
        String surname,
        AddressDto address,
        LocalDate dateOfBirth,
        Gender gender,
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