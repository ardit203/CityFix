package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.projection.UserPageableProjection;

import java.util.List;

public record DisplayUserPageableDto(
        Long id,
        String username,
        Role role,
        String name,
        String surname
) {
    public static DisplayUserPageableDto from(UserPageableProjection projection) {
        return new DisplayUserPageableDto(
                projection.getId(),
                projection.getUsername(),
                projection.getRole(),
                projection.getName(),
                projection.getSurname()
        );
    }

    public static List<DisplayUserPageableDto> from(List<UserPageableProjection> projections) {
        return projections
                .stream()
                .map(DisplayUserPageableDto::from)
                .toList();
    }
}
