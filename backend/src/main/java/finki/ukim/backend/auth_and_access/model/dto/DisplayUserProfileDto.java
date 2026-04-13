package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.UserProfile;

public record DisplayUserProfileDto(
        Long userId,
        String name,
        String surname,
        String street,
        String city,
        String postalCode
) {
    public static DisplayUserProfileDto from(UserProfile userProfile) {
        return new DisplayUserProfileDto(
                userProfile.getUser().getId(),
                userProfile.getName(),
                userProfile.getSurname(),
                userProfile.getAddress().getStreet(),
                userProfile.getAddress().getCity(),
                userProfile.getAddress().getPostalCode()
        );
    }
}
