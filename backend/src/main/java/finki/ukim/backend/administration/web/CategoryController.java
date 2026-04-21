package finki.ukim.backend.administration.web;

import finki.ukim.backend.administration.model.dto.CreateCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayCategoryDto;
import finki.ukim.backend.administration.service.application.CategoryApplicationService;
import lombok.AllArgsConstructor;
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

    @GetMapping("/{id}")
    public ResponseEntity<DisplayCategoryDto> findById(@PathVariable Long id) {
        return categoryApplicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<DisplayBasicCategoryDto> findByName(@PathVariable String name) {
        return categoryApplicationService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<DisplayCategoryDto>> findByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(categoryApplicationService.findByDepartmentId(departmentId));
    }

    @PostMapping
    public ResponseEntity<DisplayCategoryDto> create(@RequestBody CreateCategoryDto createCategoryDto) {
        return ResponseEntity.ok(categoryApplicationService.create(createCategoryDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisplayCategoryDto> update(@PathVariable Long id, @RequestBody CreateCategoryDto createCategoryDto) {
        return categoryApplicationService.update(id, createCategoryDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayBasicCategoryDto> delete(@PathVariable Long id) {
        return categoryApplicationService.deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
