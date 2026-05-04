package finki.ukim.backend.auth_and_access.web.controller;


import finki.ukim.backend.auth_and_access.model.dto.AdminUpdateUserDto;
import finki.ukim.backend.auth_and_access.model.dto.DisplayUserBasicDto;
import finki.ukim.backend.auth_and_access.model.dto.DisplayUserDto;
import finki.ukim.backend.auth_and_access.model.dto.DisplayUserPageableDto;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.service.application.UserApplicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class UserAdminController {
    private final UserApplicationService userApplicationService;


    @GetMapping
    public ResponseEntity<List<DisplayUserBasicDto>> findAll() {
        return ResponseEntity.ok(userApplicationService.findAll());
    }


    @GetMapping("/paged")
    public ResponseEntity<Page<DisplayUserPageableDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Role role
    ) {
        return ResponseEntity.ok(
                userApplicationService.findAll(page, size, sortBy, id, username, email, role)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayUserDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userApplicationService.findById(id));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayUserDto> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(userApplicationService.deleteById(id));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<DisplayUserDto> changeRole(
            @PathVariable Long id,
            @RequestParam Role role
    ) {
        return ResponseEntity.ok(userApplicationService.changeRole(id, role));
    }

    @PatchMapping("/{id}/lock")
    public ResponseEntity<DisplayUserDto> lock(
            @PathVariable Long id,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime until
    ) {
        return ResponseEntity.ok(userApplicationService.lock(id, until));
    }

    @PatchMapping("/{id}/unlock")
    public ResponseEntity<DisplayUserDto> unlock(@PathVariable Long id) {
        return ResponseEntity.ok(userApplicationService.unlock(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DisplayUserDto> adminUpdate(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateUserDto adminUpdateUserDto
    ) {
        return ResponseEntity.ok(userApplicationService.adminUpdate(id, adminUpdateUserDto));
    }
}
