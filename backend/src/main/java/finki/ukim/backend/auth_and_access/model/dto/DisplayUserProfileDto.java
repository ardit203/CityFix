package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Gender;

import java.time.LocalDate;

public record DisplayUserProfileDto(
        Long userId,
        String name,
        String surname,
        String street,
        String city,
        String postalCode,
        LocalDate dateOfBirth,
        Gender gender,
        String phoneNumber,
        String profilePictureUrl
) {
    public static DisplayUserProfileDto from(UserProfile userProfile) {
        return new DisplayUserProfileDto(
                userProfile.getUserId(),
                userProfile.getName(),
                userProfile.getSurname(),
                userProfile.getAddress().getStreet(),
                userProfile.getAddress().getCity(),
                userProfile.getAddress().getPostalCode(),
                userProfile.getDateOfBirth(),
                userProfile.getGender(),
                userProfile.getPhoneNumber(),
                userProfile.getProfilePicture() == null ? null : userProfile.getProfilePicture().getFileUrl()
        );
    }
}
