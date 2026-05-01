package finki.ukim.backend.administration.web.controller;

import finki.ukim.backend.administration.model.dto.CreateCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayCategoryPageableDto;
import finki.ukim.backend.administration.service.application.CategoryApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryApplicationService categoryApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayBasicCategoryDto>> findAll() {
        return ResponseEntity.ok(categoryApplicationService.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<DisplayCategoryPageableDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Long departmentId
    ) {
        return ResponseEntity.ok(
                categoryApplicationService.findAll(
                        page,
                        size,
                        sortBy,
                        id,
                        text,
                        departmentId
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayCategoryDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryApplicationService.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<DisplayBasicCategoryDto> findByName(@PathVariable String name) {
        return ResponseEntity.ok(categoryApplicationService.findByName(name));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<DisplayCategoryDto>> findByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(categoryApplicationService.findByDepartmentId(departmentId));
    }

    @PostMapping
    public ResponseEntity<DisplayCategoryDto> create(@Valid @RequestBody CreateCategoryDto createCategoryDto) {
        return ResponseEntity.ok(categoryApplicationService.create(createCategoryDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisplayCategoryDto> update(@PathVariable Long id, @Valid @RequestBody CreateCategoryDto createCategoryDto) {
        return ResponseEntity.ok(categoryApplicationService.update(id, createCategoryDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayBasicCategoryDto> delete(@PathVariable Long id) {
        return ResponseEntity.ok(categoryApplicationService.deleteById(id));
    }
}
