package finki.ukim.backend.auth_and_access.service.application.impl;

import finki.ukim.backend.auth_and_access.helper.JwtHelper;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.*;
import finki.ukim.backend.auth_and_access.model.exception.UserNotFoundException;
import finki.ukim.backend.auth_and_access.service.application.UserApplicationService;
import finki.ukim.backend.auth_and_access.service.application.UserProfileApplicationService;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserApplicationServiceImpl implements UserApplicationService {
    private final UserService userService;
    private final JwtHelper jwtHelper;
    private final UserProfileApplicationService userProfileApplicationService;

    @Override
    public Optional<DisplayUserDto> register(CreateUserDto createUserDto) {
        User user = userService.register(createUserDto.toUser(), createUserDto.confirmPassword());
        DisplayUserProfileDto displayUserProfileDto = userProfileApplicationService
                .create(
                        user, createUserDto.userProfile()
                );
        return Optional.of(DisplayUserDto.from(user, displayUserProfileDto));
    }

    @Override
    public Optional<LoginUserResponseDto> login(LoginUserRequestDto loginUserRequestDto) {
        User user = userService.login(loginUserRequestDto.username(), loginUserRequestDto.password());

        String token = jwtHelper.generateToken(user);

        return Optional.of(new LoginUserResponseDto(token));
    }

    @Override
    public Optional<DisplayBasicUserDto> findByUsername(String username) {
        return userService
                .findByUsername(username)
                .map(DisplayBasicUserDto::from);
    }

    @Override
    public Optional<DisplayBasicUserDto> findByEmail(String email) {
        return userService
                .findByEmail(email)
                .map(DisplayBasicUserDto::from);
    }

    @Override
    public Optional<DisplayBasicUserDto> update(Long id, CreateBasicUserDto createBasicUserDto) {
        return userService
                .update(
                        id,
                        createBasicUserDto.username(),
                        createBasicUserDto.email(),
                        createBasicUserDto.role(),
                        createBasicUserDto.notificationsEnabled()
                )
                .map(DisplayBasicUserDto::from);
    }

    @Override
    public Optional<DisplayBasicUserDto> changePassword(String username, ChangePasswordDto changePasswordDto) {
        return userService
                .changePassword(
                        username,
                        changePasswordDto.currentPassword(),
                        changePasswordDto.newPassword(),
                        changePasswordDto.confirmNewPassword()
                )
                .map(DisplayBasicUserDto::from);
    }

    @Override
    public Optional<DisplayBasicUserDto> deleteByUsername(String username) {
        return userService.deleteByUsername(username)
                .map(DisplayBasicUserDto::from);
    }

    @Override
    public Optional<DisplayBasicUserDto> deleteById(Long id) {
        return userService.deleteById(id)
                .map(DisplayBasicUserDto::from);
    }
}
