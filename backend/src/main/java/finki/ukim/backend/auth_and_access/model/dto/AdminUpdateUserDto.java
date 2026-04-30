package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Gender;
import finki.ukim.backend.auth_and_access.model.enums.Role;

import java.time.LocalDate;

public record AdminUpdateUserDto(
        String username,
        String email,
//        Role role,
        Boolean notificationsEnabled,
//        Integer failedLoginAttempts,
//        LocalDateTime lockedUntil,
//        Integer lockLevel,

        String name,
        String surname,
        AddressDto address,
        LocalDate dateOfBirth,
        Gender gender,
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