package finki.ukim.backend.auth_and_access.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.*;
import finki.ukim.backend.auth_and_access.model.dto.filter.UserFilterDto;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.service.application.UserApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserController {
    private final UserApplicationService userApplicationService;

    @GetMapping("/me")
    public ResponseEntity<DisplayUserDto> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userApplicationService.findById(user.getId()));
    }

    @PatchMapping("/me/account")
    public ResponseEntity<DisplayUserDto> updateAccount(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateMyAccountDto updateMyAccountDto
    ) {
        return ResponseEntity.ok(userApplicationService.updateAccount(user.getId(), updateMyAccountDto));
    }

    @PatchMapping("/me/profile")
    public ResponseEntity<DisplayUserDto> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateMyProfileDto updateMyProfileDto
    ) {
        return ResponseEntity.ok(userApplicationService.updateProfile(user.getId(), updateMyProfileDto));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<DisplayUserDto> changePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChangePasswordDto changePasswordDto
    ) {
        return ResponseEntity.ok(userApplicationService.changePassword(user.getId(), changePasswordDto));
    }

    @PatchMapping("/me/profile-picture")
    public ResponseEntity<DisplayUserDto> updateProfilePicture(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(userApplicationService.updateProfilePicture(user.getId(), file));
    }

    @DeleteMapping("/me/profile-picture")
    public ResponseEntity<DisplayUserDto> deleteProfilePicture(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userApplicationService.deleteProfilePicture(user.getId()));
    }

    //ADMIN

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<DisplayUserBasicDto>> findAll() {
        return ResponseEntity.ok(userApplicationService.findAll());
    }


    @GetMapping("/paged")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Page<DisplayUserPageableDto>> findAll(
            @ModelAttribute UserFilterDto userFilterDto
    ) {
        return ResponseEntity.ok(
                userApplicationService.findAll(userFilterDto)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<DisplayUserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userApplicationService.findById(id));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<DisplayUserDto> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(userApplicationService.deleteById(id));
    }

    @DeleteMapping("/bulk")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteBulk(@RequestBody List<Long> ids) {
        userApplicationService.deleteAllById(ids);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<DisplayUserDto> changeRole(
            @PathVariable Long id,
            @RequestParam Role role
    ) {
        return ResponseEntity.ok(userApplicationService.changeRole(id, role));
    }

    @PatchMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<DisplayUserDto> lock(
            @PathVariable Long id,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime until
    ) {
        return ResponseEntity.ok(userApplicationService.lock(id, until));
    }

    @PatchMapping("/{id}/unlock")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<DisplayUserDto> unlock(@PathVariable Long id) {
        return ResponseEntity.ok(userApplicationService.unlock(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<DisplayUserDto> adminUpdate(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateUserDto adminUpdateUserDto
    ) {
        return ResponseEntity.ok(userApplicationService.adminUpdate(id, adminUpdateUserDto));
    }

}