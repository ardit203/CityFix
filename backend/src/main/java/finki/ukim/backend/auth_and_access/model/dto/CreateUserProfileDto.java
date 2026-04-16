package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.Address;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Gender;

import java.time.LocalDate;

public record CreateUserProfileDto(
        String name,
        String surname,
        String street,
        String city,
        String postalCode,
        LocalDate dateOfBirth,
        Gender gender,
        String phoneNumber
) {
    public UserProfile toUserProfile(User user) {
        return new UserProfile(
                user,
                name,
                surname,
                new Address(street, city, postalCode),
                dateOfBirth,
                gender,
                phoneNumber
        );
    }
}
