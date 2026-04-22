package finki.ukim.backend.auth_and_access.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.CreateUserProfileDto;
import finki.ukim.backend.auth_and_access.model.dto.DisplayUserDto;
import finki.ukim.backend.auth_and_access.service.application.UserProfileApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@AllArgsConstructor
@RequestMapping("/api/user-profile")
public class UserProfileController {
    private final UserProfileApplicationService userProfileApplicationService;


    @GetMapping
    public ResponseEntity<DisplayUserDto> find(@AuthenticationPrincipal User user) {
        return userProfileApplicationService
                .findByUserId(user.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<DisplayUserDto> findByUserId(@PathVariable Long userId) {
        return userProfileApplicationService
                .findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<DisplayUserDto> findByUsername(@PathVariable String username) {
        return userProfileApplicationService
                .findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/update/{userId}")
    public ResponseEntity<DisplayUserDto> update(@PathVariable Long userId, @RequestBody CreateUserProfileDto createUserProfileDto) {
        return userProfileApplicationService
                .update(userId, createUserProfileDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/update-profile-picture/{userId}")
    public ResponseEntity<DisplayUserDto> updateProfilePicture(@PathVariable Long userId, @RequestBody MultipartFile profilePicture) {
        return userProfileApplicationService
                .updateProfilePicture(userId, profilePicture)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}