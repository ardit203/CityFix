package finki.ukim.backend.auth_and_access.service.application.impl;

import finki.ukim.backend.auth_and_access.helper.JwtHelper;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.*;
import finki.ukim.backend.auth_and_access.service.application.UserApplicationService;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserApplicationServiceImpl implements UserApplicationService {
    private final UserService userService;
    private final JwtHelper jwtHelper;

    @Override
    public Optional<RegisterUserResponseDto> register(RegisterUserRequestDto registerUserRequestDto) {
        User user = userService.register(registerUserRequestDto.toUser());
        return Optional.of(RegisterUserResponseDto.from(user));
    }

    @Override
    public Optional<LoginUserResponseDto> login(LoginUserRequestDto loginUserRequestDto) {
        User user = userService.login(loginUserRequestDto.username(), loginUserRequestDto.password());

        String token = jwtHelper.generateToken(user);

        return Optional.of(new LoginUserResponseDto(token));
    }

    @Override
    public Optional<RegisterUserResponseDto> findByUsername(String username) {
        return userService.findByUsername(username).map(RegisterUserResponseDto::from);
    }

    @Override
    public Optional<RegisterUserResponseDto> findByEmail(String email) {
        return userService.findByEmail(email).map(RegisterUserResponseDto::from);
    }

    @Override
    public Optional<RegisterUserResponseDto> update(String username, RegisterUserRequestDto registerUserRequestDto) {
        return userService
                .update(username, registerUserRequestDto.toUser())
                .map(RegisterUserResponseDto::from);
    }

    @Override
    public Optional<RegisterUserResponseDto> changePassword(String username, ChangePasswordDto changePasswordDto) {
        return userService
                .changePassword(
                        username, changePasswordDto.currentPassword(),
                        changePasswordDto.newPassword(),
                        changePasswordDto.confirmNewPassword()
                )
                .map(RegisterUserResponseDto::from);
    }
}
