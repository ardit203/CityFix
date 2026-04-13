package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Role;

public record RegisterUserResponseDto(
        String username,
        String email,
        Role role,
        String name,
        String surname
) {
    public static RegisterUserResponseDto from(User user, UserProfile userProfile) {
        return new RegisterUserResponseDto(
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                userProfile.getName(),
                userProfile.getSurname()
        );
    }
}
