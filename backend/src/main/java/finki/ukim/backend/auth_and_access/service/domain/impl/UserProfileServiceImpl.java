package finki.ukim.backend.auth_and_access.service.domain.impl;

import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.repository.UserProfileRepository;
import finki.ukim.backend.auth_and_access.service.domain.UserProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;

    @Override
    public Optional<UserProfile> findByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }

    @Override
    public Optional<UserProfile> findByUserIdWithUser(Long userId) {
        return userProfileRepository.findByUserIdWithUser(userId);
    }

    @Override
    public Optional<UserProfile> findByUsername(String username) {
        return userProfileRepository.findByUsername(username);
    }

    @Override
    public UserProfile create(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public Optional<UserProfile> update(Long userId, UserProfile updatedProfile) {
        return userProfileRepository
                .findById(userId)
                .map(existingProfile -> {
                    existingProfile.setName(updatedProfile.getName());
                    existingProfile.setSurname(updatedProfile.getSurname());
                    existingProfile.setAddress(updatedProfile.getAddress());
                    existingProfile.setGender(updatedProfile.getGender());
                    existingProfile.setDateOfBirth(updatedProfile.getDateOfBirth());
                    existingProfile.setPhoneNumber(updatedProfile.getPhoneNumber());
                    return userProfileRepository.save(existingProfile);
                });
    }
}
