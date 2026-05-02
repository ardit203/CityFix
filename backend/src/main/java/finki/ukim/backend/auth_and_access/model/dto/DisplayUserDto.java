package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;

public record DisplayUserDto(
        Long id,
        String username,
        String email,
        Role role,
        Boolean notificationsEnabled,
        Boolean locked,
        DisplayUserProfileDto profile
) {
    public static DisplayUserDto from(User user) {
        return new DisplayUserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getNotificationsEnabled(),
                user.isLocked(),
                DisplayUserProfileDto.from(user.getProfile())
        );
    }


}