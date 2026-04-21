package finki.ukim.backend.administration.web;

import finki.ukim.backend.administration.model.dto.CreateMunicipalityDto;
import finki.ukim.backend.administration.model.dto.DisplayMunicipalityDto;
import finki.ukim.backend.administration.service.application.MunicipalityApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/municipality")
public class MunicipalityController {
    private final MunicipalityApplicationService municipalityApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayMunicipalityDto>> findAll() {
        return ResponseEntity.ok(municipalityApplicationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayMunicipalityDto> findById(@PathVariable Long id) {
        return municipalityApplicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<DisplayMunicipalityDto> findByName(@PathVariable String name) {
        return municipalityApplicationService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<DisplayMunicipalityDto> findByCode(@PathVariable String code) {
        return municipalityApplicationService.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DisplayMunicipalityDto> create(@RequestBody CreateMunicipalityDto createMunicipalityDto) {
        return ResponseEntity.ok(municipalityApplicationService.create(createMunicipalityDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisplayMunicipalityDto> update(@PathVariable Long id, @RequestBody CreateMunicipalityDto createMunicipalityDto) {
        return municipalityApplicationService.update(id, createMunicipalityDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DisplayMunicipalityDto> delete(@PathVariable Long id) {
        return municipalityApplicationService.deleteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
