package finki.ukim.backend.auth_and_access.service.application;

import finki.ukim.backend.auth_and_access.model.dto.*;

import java.util.Optional;

public interface AuthApplicationService {
    DisplayUserDto register(RegisterDto registerDto);

    LoginUserResponseDto login(LoginUserRequestDto loginUserRequestDto);

    void forgotPassword(String email);

    void resetPassword(ResetPasswordDto resetPasswordDto);
}
