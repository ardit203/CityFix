package finki.ukim.backend.administration.web;

import finki.ukim.backend.administration.model.dto.CreateStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayStaffDto;
import finki.ukim.backend.administration.service.application.StaffApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/staff")
public class StaffController {
    private final StaffApplicationService staffApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayBasicStaffDto>> findAll() {
        return ResponseEntity.ok(staffApplicationService.findAll());
    }

    @GetMapping("/detailed")
    public ResponseEntity<List<DisplayStaffDto>> findAllWithAll() {
        return ResponseEntity.ok(staffApplicationService.findAllWithAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayStaffDto> findById(@PathVariable Long id) {
        return staffApplicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<DisplayStaffDto> findByUsername(@PathVariable String username) {
        return staffApplicationService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<DisplayBasicStaffDto>> findByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(staffApplicationService.findByDepartmentId(departmentId));
    }

    @GetMapping("/municipality/{municipalityId}")
    public ResponseEntity<List<DisplayBasicStaffDto>> findByMunicipalityId(@PathVariable Long municipalityId) {
        return ResponseEntity.ok(staffApplicationService.findByMunicipalityId(municipalityId));
    }

    @PostMapping
    public ResponseEntity<DisplayStaffDto> create(@RequestBody CreateStaffDto createStaffDto) {
        return ResponseEntity.ok(staffApplicationService.create(createStaffDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisplayBasicStaffDto> update(@PathVariable Long id, @RequestBody CreateStaffDto createStaffDto) {
        return staffApplicationService.update(id, createStaffDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayBasicStaffDto> delete(@PathVariable Long id) {
        return staffApplicationService.deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
