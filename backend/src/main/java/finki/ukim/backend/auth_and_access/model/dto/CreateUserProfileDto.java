package finki.ukim.backend.auth_and_access.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;


public record CreateUserProfileDto(
        Long userId,
        String name,
        String surname,
        String street,
        String city,
        String postalCode
) {
    public UserProfile toUserProfile(User user) {
        return new UserProfile(user, name, surname, street, city, postalCode);
    }
}
