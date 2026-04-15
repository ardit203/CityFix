package finki.ukim.backend.auth_and_access.service.application;

import finki.ukim.backend.auth_and_access.model.dto.*;

import java.util.Optional;

public interface UserApplicationService {
    Optional<RegisterUserResponseDto> register(RegisterUserRequestDto registerUserRequestDto);

    Optional<LoginUserResponseDto> login(LoginUserRequestDto loginUserRequestDto);

    Optional<RegisterUserResponseDto> findByUsername(String username);

    Optional<RegisterUserResponseDto> findByEmail(String email);

    Optional<RegisterUserResponseDto> update(String username, RegisterUserRequestDto registerUserRequestDto);

    Optional<RegisterUserResponseDto> changePassword(String username, ChangePasswordDto changePasswordDto);
}
