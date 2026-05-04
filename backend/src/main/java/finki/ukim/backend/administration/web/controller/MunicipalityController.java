package finki.ukim.backend.administration.web.controller;

import finki.ukim.backend.administration.model.dto.CreateMunicipalityDto;
import finki.ukim.backend.administration.model.dto.DisplayMunicipalityDto;
import finki.ukim.backend.administration.service.application.MunicipalityApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/municipalities")
//@PreAuthorize("hasRole('ADMINISTRATOR')")
public class MunicipalityController {
    private final MunicipalityApplicationService municipalityApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayMunicipalityDto>> findAll() {
        return ResponseEntity.ok(municipalityApplicationService.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<DisplayMunicipalityDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name
    ) {
        return ResponseEntity.ok(
                municipalityApplicationService.findAll(
                        page,
                        size,
                        sortBy,
                        id,
                        code,
                        name
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayMunicipalityDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(municipalityApplicationService.findById(id));
    }

    @GetMapping("/by-code/{code}")
    public ResponseEntity<DisplayMunicipalityDto> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(municipalityApplicationService.findByCode(code));
    }

    @PostMapping
    public ResponseEntity<DisplayMunicipalityDto> create(@Valid @RequestBody CreateMunicipalityDto createMunicipalityDto) {
        return ResponseEntity.ok(municipalityApplicationService.create(createMunicipalityDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisplayMunicipalityDto> update(@PathVariable Long id, @Valid @RequestBody CreateMunicipalityDto createMunicipalityDto) {
        return ResponseEntity.ok(municipalityApplicationService.update(id, createMunicipalityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayMunicipalityDto> delete(@PathVariable Long id) {
        return ResponseEntity.ok(municipalityApplicationService.deleteById(id));
    }
}
