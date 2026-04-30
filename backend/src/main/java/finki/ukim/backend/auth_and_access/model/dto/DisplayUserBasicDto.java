package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.projection.UserWithIdUsernameAndEmail;

import java.util.List;

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

    public static List<DisplayUserBasicDto> from(List<User> users) {
        return users
                .stream()
                .map(DisplayUserBasicDto::from)
                .toList();
    }

    public static List<DisplayUserBasicDto> fromProjection(List<UserWithIdUsernameAndEmail> users) {
        return users
                .stream()
                .map(
                        u -> new DisplayUserBasicDto(
                                u.getId(),
                                u.getUsername(),
                                u.getEmail()
                        )
                )
                .toList();
    }
}
