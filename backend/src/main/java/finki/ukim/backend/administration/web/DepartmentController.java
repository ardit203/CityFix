package finki.ukim.backend.administration.web;

import finki.ukim.backend.administration.model.dto.CreateDepartmentDto;
import finki.ukim.backend.administration.model.dto.DisplayDepartmentDto;
import finki.ukim.backend.administration.service.application.DepartmentApplicationService;
import lombok.AllArgsConstructor;
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

    @GetMapping("/{id}")
    public ResponseEntity<DisplayDepartmentDto> findById(@PathVariable Long id) {
        return departmentApplicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<DisplayDepartmentDto> findByName(@PathVariable String name) {
        return departmentApplicationService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DisplayDepartmentDto> create(@RequestBody CreateDepartmentDto createDepartmentDto) {
        return ResponseEntity.ok(departmentApplicationService.create(createDepartmentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisplayDepartmentDto> update(@PathVariable Long id, @RequestBody CreateDepartmentDto createDepartmentDto) {
        return departmentApplicationService.update(id, createDepartmentDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayDepartmentDto> delete(@PathVariable Long id) {
        return departmentApplicationService.deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
