package finki.ukim.backend.auth_and_access.service.domain.impl;

import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.repository.UserProfileRepository;
import finki.ukim.backend.auth_and_access.service.domain.UserProfileService;
import finki.ukim.backend.file_handling.model.domain.File;
import finki.ukim.backend.file_handling.model.exception.FileErrorException;
import finki.ukim.backend.file_handling.service.domain.FileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final FileService fileService;

    @Override
    public Optional<UserProfile> findByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }

    @Override
    public Optional<UserProfile> findByUsername(String username) {
        return userProfileRepository.findByUser_Username(username);
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

    @Override
    @Transactional
    public Optional<UserProfile> updateProfilePicture(Long userId, MultipartFile profilePicture) {
        Optional<UserProfile> optionalProfile = findByUserId(userId);

        if (optionalProfile.isEmpty()) {
            return Optional.empty();
        }

        UserProfile existingProfile = optionalProfile.get();

        File file;
        File existingFile = existingProfile.getProfilePicture();

        if (existingFile != null) {
            file = fileService.update(existingFile.getId(), profilePicture)
                    .orElseThrow(() -> new FileErrorException(
                            "Could not update profile picture for file id " + existingFile.getId()
                    ));
        } else {
            file = fileService.create(profilePicture);
        }

        existingProfile.setProfilePicture(file);

        UserProfile savedProfile = userProfileRepository.save(existingProfile);
        return Optional.of(savedProfile);
    }
}
