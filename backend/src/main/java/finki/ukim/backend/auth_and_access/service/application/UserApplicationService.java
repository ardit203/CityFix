package finki.ukim.backend.auth_and_access.service.application;

import finki.ukim.backend.auth_and_access.model.dto.*;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.projection.UserWithIdUsernameAndEmail;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface UserApplicationService {
    DisplayUserDto findById(Long id);

    DisplayUserDto updateAccount(Long id, UpdateMyAccountDto updateMyAccountDto);

    DisplayUserDto updateProfile(Long id, UpdateMyProfileDto updateMyProfileDto);

    DisplayUserDto changePassword(Long id, ChangePasswordDto changePasswordDto);

    DisplayUserDto updateProfilePicture(Long id, MultipartFile file);

    DisplayUserDto deleteProfilePicture(Long id);

    List<DisplayUserBasicDto> findAll();

    Page<DisplayUserPageableDto> findAll(int page, int size, String sortBy, Long id, String username, String email, Role role);

    DisplayUserDto deleteById(Long id);

    DisplayUserDto changeRole(Long id, Role role);

    DisplayUserDto lock(Long id, LocalDateTime until);

    DisplayUserDto unlock(Long id);

    DisplayUserDto adminUpdate(Long id, AdminUpdateUserDto adminUpdateUserDto);
}