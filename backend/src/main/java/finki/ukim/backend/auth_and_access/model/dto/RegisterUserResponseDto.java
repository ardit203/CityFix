package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;

import java.time.LocalDateTime;

public record RegisterUserResponseDto(
        String username,
        String email,
        Role role,
        String name,
        String surname,
        String profilePictureUrl
) {
    public static RegisterUserResponseDto from(User user) {
        return new RegisterUserResponseDto(
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getName(),
                user.getSurname(),
                user.getProfilePictureUrl()
        );
    }
}
