package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.projection.UserProjection;

import java.util.List;

public record DisplayUserPageableDto(
        Long id,
        String username,
        String email,
        Role role,
        String name,
        String surname,
        String profilePictureUrl
) {
    public static DisplayUserPageableDto from(UserProjection projection) {
        return new DisplayUserPageableDto(
                projection.getId(),
                projection.getUsername(),
                projection.getEmail(),
                projection.getRole(),
                projection.getName(),
                projection.getSurname(),
                projection.getProfilePictureUrl()
        );
    }

    public static List<DisplayUserPageableDto> from(List<UserProjection> projections) {
        return projections
                .stream()
                .map(DisplayUserPageableDto::from)
                .toList();
    }
}
