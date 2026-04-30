package finki.ukim.backend.administration.service.application.impl;

import finki.ukim.backend.administration.model.dto.CreateMunicipalityDto;
import finki.ukim.backend.administration.model.dto.DisplayMunicipalityDto;
import finki.ukim.backend.administration.service.application.MunicipalityApplicationService;
import finki.ukim.backend.administration.service.domain.MunicipalityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
    public DisplayMunicipalityDto findById(Long id) {
        return DisplayMunicipalityDto.from(municipalityService.findById(id));
    }

    @Override
    public DisplayMunicipalityDto findByCode(String code) {
        return DisplayMunicipalityDto.from(municipalityService.findByCode(code));
    }

    @Override
    public DisplayMunicipalityDto create(CreateMunicipalityDto createMunicipalityDto) {
        return DisplayMunicipalityDto.from(
                municipalityService.create(createMunicipalityDto.toMunicipality())
        );
    }

    @Override
    public DisplayMunicipalityDto update(Long id, CreateMunicipalityDto createMunicipalityDto) {
        return DisplayMunicipalityDto.from(municipalityService.update(id, createMunicipalityDto.toMunicipality()));
    }

    @Override
    public DisplayMunicipalityDto deleteById(Long id) {
        return DisplayMunicipalityDto.from(municipalityService.deleteById(id));
    }

    @Override
    public Page<DisplayMunicipalityDto> findAll(int page, int size, String sortBy, Long id, String code, String name) {
        return municipalityService
                .findAll(page, size, sortBy, id, code, name)
                .map(DisplayMunicipalityDto::from);
    }
}
