package finki.ukim.backend.administration.web.controller;

import finki.ukim.backend.administration.model.dto.CreateDepartmentDto;
import finki.ukim.backend.administration.model.dto.DisplayDepartmentDto;
import finki.ukim.backend.administration.model.dto.filters.DepartmentFilterDto;
import finki.ukim.backend.administration.service.application.DepartmentApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/departments")
//@PreAuthorize("hasRole('ADMINISTRATOR')")
public class DepartmentController {
    private final DepartmentApplicationService departmentApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayDepartmentDto>> findAll() {
        return ResponseEntity.ok(departmentApplicationService.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<DisplayDepartmentDto>> findAll(
            @ModelAttribute DepartmentFilterDto departmentFilterDto
            ) throws InterruptedException {
        return ResponseEntity.ok(
                departmentApplicationService.findAll(
                        departmentFilterDto
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayDepartmentDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentApplicationService.findById(id));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<DisplayDepartmentDto> findByName(@PathVariable String name) {
        return ResponseEntity.ok(departmentApplicationService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<DisplayDepartmentDto> create(@Valid @RequestBody CreateDepartmentDto createDepartmentDto) {
        return ResponseEntity.ok(departmentApplicationService.create(createDepartmentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisplayDepartmentDto> update(@PathVariable Long id, @Valid @RequestBody CreateDepartmentDto createDepartmentDto) {
        return ResponseEntity.ok(departmentApplicationService.update(id, createDepartmentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayDepartmentDto> delete(@PathVariable Long id) {
        return ResponseEntity.ok(departmentApplicationService.deleteById(id));
    }
}
