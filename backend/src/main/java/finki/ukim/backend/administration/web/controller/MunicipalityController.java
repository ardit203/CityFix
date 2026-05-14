package finki.ukim.backend.administration.web.controller;

import finki.ukim.backend.administration.model.dto.CreateMunicipalityDto;
import finki.ukim.backend.administration.model.dto.DisplayMunicipalityDto;
import finki.ukim.backend.administration.model.dto.filters.MunicipalityFilterDto;
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
public class MunicipalityController {
    private final MunicipalityApplicationService municipalityApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayMunicipalityDto>> findAll() {
        return ResponseEntity.ok(municipalityApplicationService.findAll());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/paged")
    public ResponseEntity<Page<DisplayMunicipalityDto>> findAll(
            @ModelAttribute MunicipalityFilterDto municipalityFilterDto
            ) {
        return ResponseEntity.ok(
                municipalityApplicationService.findAll(
                        municipalityFilterDto
                )
        );
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<DisplayMunicipalityDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(municipalityApplicationService.findById(id));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/by-code/{code}")
    public ResponseEntity<DisplayMunicipalityDto> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(municipalityApplicationService.findByCode(code));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    public ResponseEntity<DisplayMunicipalityDto> create(@Valid @RequestBody CreateMunicipalityDto createMunicipalityDto) {
        return ResponseEntity.ok(municipalityApplicationService.create(createMunicipalityDto));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<DisplayMunicipalityDto> update(@PathVariable Long id, @Valid @RequestBody CreateMunicipalityDto createMunicipalityDto) {
        return ResponseEntity.ok(municipalityApplicationService.update(id, createMunicipalityDto));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayMunicipalityDto> delete(@PathVariable Long id) {
        return ResponseEntity.ok(municipalityApplicationService.deleteById(id));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteBulk(@RequestBody List<Long> ids) {
        municipalityApplicationService.deleteAllById(ids);
        return ResponseEntity.noContent().build();
    }
}
