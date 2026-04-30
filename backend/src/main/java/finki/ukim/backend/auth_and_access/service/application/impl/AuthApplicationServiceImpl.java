package finki.ukim.backend.auth_and_access.service.application.impl;

import finki.ukim.backend.auth_and_access.helper.JwtHelper;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import finki.ukim.backend.auth_and_access.model.dto.*;
import finki.ukim.backend.auth_and_access.service.application.AuthApplicationService;
import finki.ukim.backend.auth_and_access.service.domain.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthApplicationServiceImpl implements AuthApplicationService {
    private final AuthService authService;
    private final JwtHelper jwtHelper;

    public DisplayUserDto register(RegisterDto registerDto) {
        User user = registerDto.toUser();
        UserProfile profile = registerDto.toUserProfile();
        user.setProfile(profile);

        User savedUser = authService.register(user, registerDto.confirmPassword());

        return DisplayUserDto.from(savedUser);
    }

    @Override
    public LoginUserResponseDto login(LoginUserRequestDto loginUserRequestDto) {
        User user = authService.login(loginUserRequestDto.username(), loginUserRequestDto.password());
        String token = jwtHelper.generateToken(user);
        return new LoginUserResponseDto(token);
    }

    @Override
    public void forgotPassword(String email) {
        authService.forgotPassword(email);
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        authService.resetPassword(
                resetPasswordDto.token(),
                resetPasswordDto.newPassword(),
                resetPasswordDto.confirmPassword()
        );
    }
}
