package finki.ukim.backend.administration.service.application.impl;

import finki.ukim.backend.administration.model.dto.CreateMunicipalityDto;
import finki.ukim.backend.administration.model.dto.DisplayMunicipalityDto;
import finki.ukim.backend.administration.service.application.MunicipalityApplicationService;
import finki.ukim.backend.administration.service.domain.MunicipalityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MunicipalityApplicationServiceImpl implements MunicipalityApplicationService {
    private final MunicipalityService municipalityService;

    @Override
    public List<DisplayMunicipalityDto> findAll() {
        return DisplayMunicipalityDto.from(municipalityService.findAll());
    }

    @Override
    public Optional<DisplayMunicipalityDto> findById(Long id) {
        return municipalityService.findById(id).map(DisplayMunicipalityDto::from);
    }

    @Override
    public Optional<DisplayMunicipalityDto> findByName(String name) {
        return municipalityService.findByName(name).map(DisplayMunicipalityDto::from);
    }

    @Override
    public Optional<DisplayMunicipalityDto> findByCode(String code) {
        return municipalityService.findByCode(code).map(DisplayMunicipalityDto::from);
    }

    @Override
    public DisplayMunicipalityDto create(CreateMunicipalityDto createMunicipalityDto) {
        return DisplayMunicipalityDto.from(
                municipalityService.create(createMunicipalityDto.toMunicipality())
        );
    }

    @Override
    public Optional<DisplayMunicipalityDto> update(Long id, CreateMunicipalityDto createMunicipalityDto) {
        return municipalityService
                .update(id, createMunicipalityDto.toMunicipality())
                .map(DisplayMunicipalityDto::from);
    }

    @Override
    public Optional<DisplayMunicipalityDto> deleteById(Long id) {
        return municipalityService
                .deleteById(id)
                .map(DisplayMunicipalityDto::from);
    }
}
