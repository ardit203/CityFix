package finki.ukim.backend.administration.web.controller;

import finki.ukim.backend.administration.model.dto.CreateStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayStaffPageableDto;
import finki.ukim.backend.administration.service.application.StaffApplicationService;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.DisplayUserPageableDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/staff")
public class StaffController {
    private final StaffApplicationService staffApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayBasicStaffDto>> findAll(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(staffApplicationService.findAll(currentUser));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<DisplayStaffPageableDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long municipalityId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String municipalityCode,
            @RequestParam(required = false) String municipalityName,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(
                staffApplicationService.findAll(
                        currentUser,
                        page,
                        size,
                        sortBy,
                        id,
                        userId,
                        departmentId,
                        municipalityId,
                        username,
                        municipalityCode,
                        municipalityName
                )
        );
    }


    @GetMapping("/detailed")
    public ResponseEntity<List<DisplayStaffDto>> findAllWithAll(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(staffApplicationService.findAllWithAll(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayStaffDto> findById(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(staffApplicationService.findById(id, currentUser));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<DisplayStaffDto> findByUserId(@PathVariable Long userId, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(staffApplicationService.findByUserId(currentUser, userId));
    }

    @GetMapping("/by-department/{departmentId}")
//    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<DisplayBasicStaffDto>> findByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(staffApplicationService.findByDepartmentId(departmentId));
    }

    @GetMapping("/by-municipality/{municipalityId}")
//    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<DisplayBasicStaffDto>> findByMunicipalityId(@PathVariable Long municipalityId) {
        return ResponseEntity.ok(staffApplicationService.findByMunicipalityId(municipalityId));
    }

    @GetMapping("/available-users")
    public ResponseEntity<List<DisplayUserPageableDto>> findUsersAvailableForStaff(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(staffApplicationService.findUsersAvailableForStaff(currentUser));
    }

    @PostMapping
    public ResponseEntity<DisplayStaffDto> create(@Valid @RequestBody CreateStaffDto createStaffDto, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(staffApplicationService.create(currentUser, createStaffDto));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<DisplayBasicStaffDto> update(@PathVariable Long id, @Valid @RequestBody CreateStaffDto createStaffDto) {
        return ResponseEntity.ok(staffApplicationService.update(id, createStaffDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayBasicStaffDto> delete(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(staffApplicationService.deleteById(id, currentUser));
    }
}
