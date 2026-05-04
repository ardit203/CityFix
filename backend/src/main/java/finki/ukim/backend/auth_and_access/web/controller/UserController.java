package finki.ukim.backend.auth_and_access.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.*;
import finki.ukim.backend.auth_and_access.service.application.UserApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
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


}