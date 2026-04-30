package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;

public record DisplayUserBasicDto(
        Long id,
        String username,
        String email
) {
    public static DisplayUserBasicDto from(User user) {
        return new DisplayUserBasicDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
