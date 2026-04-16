package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;

public record DisplayBasicUserDto(
        Long id,
        String username,
        String email,
        Role role,
        Boolean notificationsEnabled
) {
    public static DisplayBasicUserDto from(User user) {
        return new DisplayBasicUserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getNotificationsEnabled()
        );
    }
}
