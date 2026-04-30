package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Gender;

import java.time.LocalDate;

public record DisplayUserProfileDto(
        String name,
        String surname,
        AddressDto address,
        LocalDate dateOfBirth,
        Gender gender,
        String phoneNumber,
        String profilePictureUrl
) {
    public static DisplayUserProfileDto from(UserProfile userProfile) {
        return new DisplayUserProfileDto(
                userProfile.getName(),
                userProfile.getSurname(),
                AddressDto.from(userProfile.getAddress()),
                userProfile.getDateOfBirth(),
                userProfile.getGender(),
                userProfile.getPhoneNumber(),
                userProfile.getProfilePicture() == null ? null : userProfile.getProfilePicture().getFileUrl()
        );
    }
}