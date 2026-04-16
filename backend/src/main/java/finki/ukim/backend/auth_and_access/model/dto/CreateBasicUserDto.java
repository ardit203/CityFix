package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.enums.Role;

public record CreateBasicUserDto(
        String username,
        String email,
        Role role,
        Boolean notificationsEnabled
) {
}
