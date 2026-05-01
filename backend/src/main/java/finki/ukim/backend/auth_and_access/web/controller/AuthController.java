package finki.ukim.backend.auth_and_access.web.controller;


import finki.ukim.backend.auth_and_access.model.dto.*;
import finki.ukim.backend.auth_and_access.service.application.AuthApplicationService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthApplicationService authApplicationService;

    @PostMapping("/register")
    public ResponseEntity<DisplayUserDto> register(@Valid @RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(authApplicationService.register(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponseDto> login(
            @Valid @RequestBody LoginUserRequestDto loginUserRequestDto
    ) {
        return ResponseEntity.ok(authApplicationService.login(loginUserRequestDto));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) {
        authApplicationService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        authApplicationService.resetPassword(resetPasswordDto);
        return ResponseEntity.ok().build();
    }
}