package finki.ukim.backend.auth_and_access.service.domain;


import finki.ukim.backend.auth_and_access.model.domain.UserProfile;

import java.util.Optional;

public interface UserProfileService {
    Optional<UserProfile> findByUserId(Long userId);
    Optional<UserProfile> findByUserIdWithUser(Long userId);
    Optional<UserProfile> findByUsername(String username);
    UserProfile create(UserProfile userProfile);
    Optional<UserProfile> update(Long userId, UserProfile updatedProfile);

//    Optional<UserProfile> updateProfilePicture(Long userId, String profilePictureUrl);
}
