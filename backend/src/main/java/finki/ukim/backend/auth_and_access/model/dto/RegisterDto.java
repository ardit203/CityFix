package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Gender;
import finki.ukim.backend.auth_and_access.model.enums.Role;

import java.time.LocalDate;

public record RegisterDto(
        String username,
        String email,
        String password,
        String confirmPassword,

        String name,
        String surname,
        AddressDto address,
        LocalDate dateOfBirth,
        Gender gender,
        String phoneNumber
) {
    public User toUser() {
        return new User(
                username,
                password,
                email,
                Role.ROLE_CITIZEN
        );
    }

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
