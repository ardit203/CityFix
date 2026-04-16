package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;

public record DisplayUserDto(
        Long id,
        String username,
        String email,
        Role role,
        Boolean notificationsEnabled,
        DisplayUserProfileDto userProfile
) {
    public static DisplayUserDto from(User user, DisplayUserProfileDto displayUserProfileDto) {
        return new DisplayUserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getNotificationsEnabled(),
                displayUserProfileDto
        );
    }
}
