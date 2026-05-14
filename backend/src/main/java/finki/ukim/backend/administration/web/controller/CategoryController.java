package finki.ukim.backend.administration.web.controller;

import finki.ukim.backend.administration.model.dto.CreateCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayCategoryPageableDto;
import finki.ukim.backend.administration.model.dto.filters.CategoryFilterDto;
import finki.ukim.backend.administration.service.application.CategoryApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")

public class CategoryController {
    private final CategoryApplicationService categoryApplicationService;


    @GetMapping
    public ResponseEntity<List<DisplayBasicCategoryDto>> findAll() {
        return ResponseEntity.ok(categoryApplicationService.findAll());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/paged")
    public ResponseEntity<Page<DisplayCategoryPageableDto>> findAll(
            @ModelAttribute CategoryFilterDto categoryFilterDto
            ) {
        return ResponseEntity.ok(
                categoryApplicationService.findAll(
                        categoryFilterDto
                )
        );
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<DisplayCategoryDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryApplicationService.findById(id));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/by-name/{name}")
    public ResponseEntity<DisplayBasicCategoryDto> findByName(@PathVariable String name) {
        return ResponseEntity.ok(categoryApplicationService.findByName(name));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/by-department/{departmentId}")
    public ResponseEntity<List<DisplayCategoryDto>> findByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(categoryApplicationService.findByDepartmentId(departmentId));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    public ResponseEntity<DisplayCategoryDto> create(@Valid @RequestBody CreateCategoryDto createCategoryDto) {
        return ResponseEntity.ok(categoryApplicationService.create(createCategoryDto));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<DisplayCategoryDto> update(@PathVariable Long id, @Valid @RequestBody CreateCategoryDto createCategoryDto) {
        return ResponseEntity.ok(categoryApplicationService.update(id, createCategoryDto));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayBasicCategoryDto> delete(@PathVariable Long id) {
        return ResponseEntity.ok(categoryApplicationService.deleteById(id));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteBulk(@RequestBody List<Long> ids) {
        categoryApplicationService.deleteAllById(ids);
        return ResponseEntity.noContent().build();
    }
}
