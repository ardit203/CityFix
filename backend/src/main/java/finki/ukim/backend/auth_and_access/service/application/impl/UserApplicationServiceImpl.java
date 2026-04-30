package finki.ukim.backend.auth_and_access.service.application.impl;

import finki.ukim.backend.auth_and_access.model.dto.*;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.projection.UserWithIdUsernameAndEmail;
import finki.ukim.backend.auth_and_access.service.application.UserApplicationService;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService {
    private final UserService userService;

    @Override
    public DisplayUserDto findById(Long id) {
        return DisplayUserDto.from(userService.findByIdWithProfileAndPic(id));
    }

    @Override
    public DisplayUserDto updateAccount(Long id, UpdateMyAccountDto updateMyAccountDto) {
        return DisplayUserDto.from(
                userService.updateAccount(id, updateMyAccountDto.toUser())
        );
    }

    @Override
    public DisplayUserDto updateProfile(Long id, UpdateMyProfileDto updateMyProfileDto) {
        return DisplayUserDto.from(
                userService.updateProfile(id, updateMyProfileDto.toUserProfile())
        );
    }

    @Override
    public DisplayUserDto changePassword(Long id, ChangePasswordDto changePasswordDto) {
        return DisplayUserDto.from(
                userService.changePassword(
                        id,
                        changePasswordDto.currentPassword(),
                        changePasswordDto.newPassword(),
                        changePasswordDto.confirmNewPassword()
                )
        );
    }

    @Override
    public DisplayUserDto updateProfilePicture(Long id, MultipartFile file) {
        return DisplayUserDto.from(
                userService.updateProfilePicture(id, file)
        );
    }

    @Override
    public DisplayUserDto deleteProfilePicture(Long id) {
        return DisplayUserDto.from(userService.deleteProfilePicture(id));
    }

    @Override
    public List<UserWithIdUsernameAndEmail> findAll() {
        return userService.findAll();
    }

    @Override
    public Page<DisplayUserPageableDto> findAll(int page, int size, String sortBy, Long id, String username, String email, Role role) {
        return userService.findAll(page, size, sortBy, id, username, email, role).map(DisplayUserPageableDto::from);
    }

    @Override
    public DisplayUserDto deleteById(Long id) {
        return DisplayUserDto.from(userService.deleteById(id));
    }

    @Override
    public DisplayUserDto changeRole(Long id, Role role) {
        return DisplayUserDto.from(userService.changeRole(id, role));
    }

    @Override
    public DisplayUserDto lock(Long id, LocalDateTime until) {
        return DisplayUserDto.from(userService.lock(id, until));
    }

    @Override
    public DisplayUserDto unlock(Long id) {
        return DisplayUserDto.from(userService.unlock(id));
    }

    @Override
    public DisplayUserDto adminUpdate(Long id, AdminUpdateUserDto adminUpdateUserDto) {
        return DisplayUserDto.from(userService.adminUpdateUser(id, adminUpdateUserDto.toUser()));
    }
}
