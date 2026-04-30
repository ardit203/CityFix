package finki.ukim.backend.auth_and_access.service.domain.impl;

import finki.ukim.backend.auth_and_access.model.domain.Address;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.exception.*;
import finki.ukim.backend.auth_and_access.model.projection.UserPageableProjection;
import finki.ukim.backend.auth_and_access.model.projection.UserWithIdUsernameAndEmail;
import finki.ukim.backend.auth_and_access.repository.UserRepository;
import finki.ukim.backend.auth_and_access.service.domain.PasswordService;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import finki.ukim.backend.file_handling.constants.FileConstants;
import finki.ukim.backend.file_handling.helper.FileHelper;
import finki.ukim.backend.file_handling.model.domain.File;
import finki.ukim.backend.file_handling.model.exception.FileErrorException;
import finki.ukim.backend.file_handling.service.domain.FileService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final FileService fileService;
    private final FileHelper fileHelper;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<UserWithIdUsernameAndEmail> findAll() {
        return userRepository.findAllWithIdUsernameAndEmail();
    }

    @Override
    public Page<UserPageableProjection> findAll(int page, int size, String sortBy, Long id, String username, String email, Role role) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).and(Sort.by("createdAt")));

        return userRepository.findFiltered(id, username, email, role, pageable);
    }

    @Override
    public User adminUpdateUser(Long id, User user) {
        User existingUser = findByIdWithProfileAndPic(id);

        if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new UsernameAlreadyExistsException(user.getUsername());
            }
            existingUser.setUsername(user.getUsername());
        }

        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new EmailAlreadyExistsException(user.getEmail());
            }
            existingUser.setEmail(user.getEmail());

            // Optional but recommended:
            // user.setEmailVerified(false);
            // send verification email
        }

        if (user.getNotificationsEnabled() != null) {
            existingUser.setNotificationsEnabled(user.getNotificationsEnabled());
        }

        if (user.getProfile() != null) {
            if (existingUser.getProfile() == null) {
                existingUser.setProfile(user.getProfile());
            } else {
                UserProfile existingProfile = existingUser.getProfile();
                UserProfile newProfile = user.getProfile();

                if (newProfile.getName() != null) {
                    existingProfile.setName(newProfile.getName());
                }
                if (newProfile.getSurname() != null) {
                    existingProfile.setSurname(newProfile.getSurname());
                }
                if (newProfile.getPhoneNumber() != null) {
                    existingProfile.setPhoneNumber(newProfile.getPhoneNumber());
                }
                if (newProfile.getAddress() != null) {
                    existingProfile.setAddress(newProfile.getAddress());
                }
                if (newProfile.getDateOfBirth() != null) {
                    existingProfile.setDateOfBirth(newProfile.getDateOfBirth());
                }
                if (newProfile.getGender() != null) {
                    existingProfile.setGender(newProfile.getGender());
                }
            }
        }

        return userRepository.save(existingUser);

    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User findByIdWithProfile(Long id) {
        return userRepository.findByIdWithProfile(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User findByIdWithProfileAndPic(Long id) {
        return userRepository.findByIdWithProfileAndPic(id).orElseThrow(() -> new UserNotFoundException(id));
    }


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateAccount(Long id, User user) {
        User existing = findByIdWithProfileAndPic(id);

        if (user.getUsername() != null && !user.getUsername().equals(existing.getUsername())) {
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new UsernameAlreadyExistsException(user.getUsername());
            }
            existing.setUsername(user.getUsername());
        }

        if (user.getEmail() != null && !user.getEmail().equals(existing.getEmail())) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new EmailAlreadyExistsException(user.getEmail());
            }
            existing.setEmail(user.getEmail());

            // Optional but recommended:
            // user.setEmailVerified(false);
            // send verification email
        }

        if (user.getNotificationsEnabled() != null) {
            existing.setNotificationsEnabled(user.getNotificationsEnabled());
        }

        return userRepository.save(existing);


    }

    @Override
    public User updateProfile(Long id, UserProfile userProfile) {
        User existing = findByIdWithProfileAndPic(id);

        if (userProfile.getName() != null) {
            existing.getProfile().setName(userProfile.getName());
        }

        if (userProfile.getSurname() != null) {
            existing.getProfile().setSurname(userProfile.getSurname());
        }

        if (userProfile.getPhoneNumber() != null) {
            existing.getProfile().setPhoneNumber(userProfile.getPhoneNumber());
        }

        if (userProfile.getAddress() != null) {
            Address address = new Address(userProfile.getAddress().getStreet(), userProfile.getAddress().getCity(), userProfile.getAddress().getPostalCode());
            existing.getProfile().setAddress(address);
        }

        if (userProfile.getDateOfBirth() != null) {
            existing.getProfile().setDateOfBirth(userProfile.getDateOfBirth());
        }

        if (userProfile.getGender() != null) {
            existing.getProfile().setGender(userProfile.getGender());
        }

        return userRepository.save(existing);

    }

    @Override
    public User updateProfilePicture(Long id, MultipartFile profilePicture) {
        if (!fileHelper.isImage(profilePicture)) {
            throw new FileErrorException("The provided profile picture is not an image file.");
        }
        User existingUser = findByIdWithProfileAndPic(id);

        File file;
        File existingFile = existingUser.getProfile().getProfilePicture();

        if (existingFile != null) {
            file = fileService.update(existingFile.getId(), profilePicture);
        } else {
            file = fileService.create(profilePicture, FileConstants.PROFILE_PIC_DIR);
        }

        existingUser.getProfile().setProfilePicture(file);

        return userRepository.save(existingUser);
    }

    @Override
    public User deleteProfilePicture(Long id) {
        User existing = findByIdWithProfileAndPic(id);
        Long fileId = existing.getProfile().getProfilePicture().getId();
        existing.getProfile().setProfilePicture(null);
        fileService.deleteById(fileId);
        return userRepository.save(existing);
    }

    @Override
    public User deleteById(Long id) {
        User user = findById(id);
        userRepository.delete(user);
        return user;
    }

    @Override
    public User changeRole(Long id, Role role) {
        User existing = findById(id);
        existing.setRole(role);
        return userRepository.save(existing);
    }

    @Override
    public User lock(Long id, LocalDateTime until) {
        User existing = findById(id);
        existing.setLockedUntil(until);
        return userRepository.save(existing);
    }

    @Override
    public User unlock(Long id) {
        User existing = findById(id);
        existing.setLockedUntil(null);
        existing.setFailedLoginAttempts(0);
        existing.setLockLevel(0);
        return userRepository.save(existing);

    }

    @Override
    public User changePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword) {
        User user = findByIdWithProfileAndPic(id);
        String encodedNewPassword = passwordService.preparePasswordForChange(
                currentPassword,
                user.getPassword(),
                newPassword,
                confirmNewPassword
        );

        user.setPassword(encodedNewPassword);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
