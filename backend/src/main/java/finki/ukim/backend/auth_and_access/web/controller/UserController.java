package finki.ukim.backend.auth_and_access.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.*;
import finki.ukim.backend.auth_and_access.service.application.UserApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserApplicationService userApplicationService;

    @GetMapping("/username/{username}")
    public ResponseEntity<DisplayBasicUserDto> findByUsername(@PathVariable String username) {
        return userApplicationService.findByUsername(username).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<DisplayBasicUserDto> findByEmail(@PathVariable String email) {
        return userApplicationService.findByEmail(email).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<DisplayBasicUserDto> me(@AuthenticationPrincipal User user) {
        return userApplicationService
                .findByUsername(user.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/register")
    public ResponseEntity<DisplayUserDto> register(@RequestBody CreateUserDto registerUserRequestDto) {
        return userApplicationService
                .register(registerUserRequestDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponseDto> login(@RequestBody LoginUserRequestDto loginUserRequestDto) {
        return userApplicationService
                .login(loginUserRequestDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<DisplayBasicUserDto> update(@PathVariable Long userId, @RequestBody CreateBasicUserDto createBasicUserDto) {
        return userApplicationService
                .update(userId, createBasicUserDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/change-password")
    public ResponseEntity<DisplayBasicUserDto> changePassword(@AuthenticationPrincipal User user, @RequestBody ChangePasswordDto changePasswordDto) {
        return userApplicationService
                .changePassword(user.getUsername(), changePasswordDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<DisplayBasicUserDto> delete(@PathVariable Long userId) {
        return userApplicationService
                .deleteById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}