package finki.ukim.backend.auth_and_access.service.application.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.CreateUserProfileDto;
import finki.ukim.backend.auth_and_access.model.dto.DisplayUserDto;
import finki.ukim.backend.auth_and_access.model.dto.DisplayUserProfileDto;
import finki.ukim.backend.auth_and_access.service.application.UserProfileApplicationService;
import finki.ukim.backend.auth_and_access.service.domain.UserProfileService;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserProfileApplicationServiceImpl implements UserProfileApplicationService {
    private final UserProfileService userProfileService;
    private final UserService userService;

    @Override
    public Optional<DisplayUserDto> findByUserId(Long userId) {
        return userProfileService.findByUserIdWithUser(userId)
                .map(up -> {
                    DisplayUserProfileDto profileDto = DisplayUserProfileDto.from(up);
                    return new DisplayUserDto(
                            up.getUser().getId(),
                            up.getUser().getUsername(),
                            up.getUser().getEmail(),
                            up.getUser().getRole(),
                            up.getUser().getNotificationsEnabled(),
                            profileDto
                    );
                });
    }

    @Override
    public Optional<DisplayUserDto> findByUsername(String username) {
        return userProfileService.findByUsername(username)
                .map(up -> {
                    DisplayUserProfileDto profileDto = DisplayUserProfileDto.from(up);
                    return new DisplayUserDto(
                            up.getUser().getId(),
                            up.getUser().getUsername(),
                            up.getUser().getEmail(),
                            up.getUser().getRole(),
                            up.getUser().getNotificationsEnabled(),
                            profileDto
                    );
                });
    }

    @Override
    public DisplayUserProfileDto create(String username, CreateUserProfileDto createUserProfileDto) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return create(user, createUserProfileDto);
    }

    @Override
    public DisplayUserProfileDto create(User user, CreateUserProfileDto createUserProfileDto) {
        return DisplayUserProfileDto.from(
                userProfileService
                        .create(createUserProfileDto.toUserProfile(user))
        );
    }

    @Override
    public Optional<DisplayUserDto> update(Long userId, CreateUserProfileDto createUserProfileDto) {
        return userProfileService
                .update(userId, createUserProfileDto.toUserProfile(null))
                .map(up -> {
                    DisplayUserProfileDto profileDto = DisplayUserProfileDto.from(up);
                    return new DisplayUserDto(
                            up.getUser().getId(),
                            up.getUser().getUsername(),
                            up.getUser().getEmail(),
                            up.getUser().getRole(),
                            up.getUser().getNotificationsEnabled(),
                            profileDto
                    );
                });
    }

    @Override
    public Optional<DisplayUserDto> updateProfilePicture(String username, MultipartFile profilePicture) {
        return Optional.empty();
    }


}
