package finki.ukim.backend.administration.web.controller;

import finki.ukim.backend.administration.model.dto.CreateStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayStaffPageableDto;
import finki.ukim.backend.administration.model.dto.filters.StaffFilterDto;
import finki.ukim.backend.administration.service.application.StaffApplicationService;
import finki.ukim.backend.reporting_and_export_import.service.domain.StaffImportService;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.DisplayUserPageableDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/staff")
@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'MANAGER')")
public class StaffController {
    private final StaffApplicationService staffApplicationService;
    private final StaffImportService staffImportService;

    @GetMapping
    public ResponseEntity<List<DisplayBasicStaffDto>> findAll(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(staffApplicationService.findAll(currentUser));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<DisplayStaffPageableDto>> findAll(
            @ModelAttribute StaffFilterDto  staffFilterDto,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(
                staffApplicationService.findAll(
                        currentUser,
                        staffFilterDto
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
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<DisplayBasicStaffDto>> findByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(staffApplicationService.findByDepartmentId(departmentId));
    }

    @GetMapping("/by-municipality/{municipalityId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
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

    @PostMapping(
            value = "/import/excel",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> importStaffFromExcel(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User currentUser
    ) {
        int importedCount = staffImportService.importStaffFromExcel(file, currentUser);
        return ResponseEntity.ok("Imported " + importedCount + " staff members successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<DisplayBasicStaffDto> update(@PathVariable Long id, @Valid @RequestBody CreateStaffDto createStaffDto) {
        return ResponseEntity.ok(staffApplicationService.update(id, createStaffDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayBasicStaffDto> delete(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(staffApplicationService.deleteById(id, currentUser));
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteBulk(@RequestBody List<Long> ids, @AuthenticationPrincipal User currentUser) {
        staffApplicationService.deleteAllById(ids, currentUser);
        return ResponseEntity.noContent().build();
    }
}
