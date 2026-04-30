package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;

public record UpdateMyAccountDto(
        String username,
        String email,
        Boolean notificationsEnabled
) {
    public User toUser() {
        return new User(
                username,
                "",
                email,
                Role.ROLE_CITIZEN,
                notificationsEnabled
        );
    }
}