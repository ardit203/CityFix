package finki.ukim.backend.administration.web;

import finki.ukim.backend.administration.model.dto.CreateDepartmentDto;
import finki.ukim.backend.administration.model.dto.DisplayDepartmentDto;
import finki.ukim.backend.administration.service.application.DepartmentApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/department")
public class DepartmentController {
    private final DepartmentApplicationService departmentApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayDepartmentDto>> findAll() {
        return ResponseEntity.ok(departmentApplicationService.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<DisplayDepartmentDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String text
    ) {
        return ResponseEntity.ok(
                departmentApplicationService.findAll(
                        page,
                        size,
                        sortBy,
                        id,
                        text
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayDepartmentDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentApplicationService.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<DisplayDepartmentDto> findByName(@PathVariable String name) {
        return ResponseEntity.ok(departmentApplicationService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<DisplayDepartmentDto> create(@RequestBody CreateDepartmentDto createDepartmentDto) {
        return ResponseEntity.ok(departmentApplicationService.create(createDepartmentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisplayDepartmentDto> update(@PathVariable Long id, @RequestBody CreateDepartmentDto createDepartmentDto) {
        return ResponseEntity.ok(departmentApplicationService.update(id, createDepartmentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayDepartmentDto> delete(@PathVariable Long id) {
        return ResponseEntity.ok(departmentApplicationService.deleteById(id));
    }
}
