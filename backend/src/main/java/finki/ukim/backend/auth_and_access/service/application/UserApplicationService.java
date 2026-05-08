package finki.ukim.backend.auth_and_access.service.application;

import finki.ukim.backend.auth_and_access.model.dto.*;
import finki.ukim.backend.auth_and_access.model.dto.filter.UserFilterDto;
import finki.ukim.backend.auth_and_access.model.enums.Role;
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

    Page<DisplayUserPageableDto> findAll(UserFilterDto userFilterDto);

    DisplayUserDto deleteById(Long id);

    DisplayUserDto changeRole(Long id, Role role);

    DisplayUserDto lock(Long id, LocalDateTime until);

    DisplayUserDto unlock(Long id);

    DisplayUserDto adminUpdate(Long id, AdminUpdateUserDto adminUpdateUserDto);
}