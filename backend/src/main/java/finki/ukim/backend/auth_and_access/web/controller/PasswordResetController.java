package finki.ukim.backend.auth_and_access.web.controller;

import finki.ukim.backend.auth_and_access.model.dto.DisplayBasicUserDto;
import finki.ukim.backend.auth_and_access.model.dto.ResetPasswordDto;
import finki.ukim.backend.auth_and_access.service.application.PasswordResetApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetApplicationService passwordResetApplicationService;

    @PostMapping("/request")
    public ResponseEntity<?> requestReset(@RequestParam String email) {
        passwordResetApplicationService.requestPasswordReset(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<DisplayBasicUserDto> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return passwordResetApplicationService.resetPassword(
                        resetPasswordDto.token(),
                        resetPasswordDto.newPassword(),
                        resetPasswordDto.confirmPassword()
                )
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}