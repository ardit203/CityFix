package finki.ukim.backend.auth_and_access.service.application;

import finki.ukim.backend.auth_and_access.model.dto.*;

import java.util.Optional;

public interface UserApplicationService {
    Optional<DisplayUserDto> register(CreateUserDto createUserDto);

    Optional<LoginUserResponseDto> login(LoginUserRequestDto loginUserRequestDto);

    Optional<DisplayBasicUserDto> findByUsername(String username);

    Optional<DisplayBasicUserDto> findByEmail(String email);

    Optional<DisplayBasicUserDto> update(Long id, CreateBasicUserDto createBasicUserDto);

    Optional<DisplayBasicUserDto> changePassword(String username, ChangePasswordDto changePasswordDto);

    Optional<DisplayBasicUserDto> deleteByUsername(String username);

    Optional<DisplayBasicUserDto> deleteById(Long id);
}
