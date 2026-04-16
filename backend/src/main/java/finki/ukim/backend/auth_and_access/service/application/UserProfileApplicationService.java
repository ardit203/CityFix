package finki.ukim.backend.auth_and_access.service.application;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.CreateUserProfileDto;
import finki.ukim.backend.auth_and_access.model.dto.DisplayUserDto;
import finki.ukim.backend.auth_and_access.model.dto.DisplayUserProfileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserProfileApplicationService {
    Optional<DisplayUserDto> findByUserId(Long userId);

    Optional<DisplayUserDto> findByUsername(String username);

    DisplayUserProfileDto create(String username, CreateUserProfileDto createUserProfileDto);

    DisplayUserProfileDto create(User user, CreateUserProfileDto createUserProfileDto);

    Optional<DisplayUserDto> update(Long userId, CreateUserProfileDto createUserProfileDto);

    Optional<DisplayUserDto> updateProfilePicture(String username, MultipartFile profilePicture);
}
