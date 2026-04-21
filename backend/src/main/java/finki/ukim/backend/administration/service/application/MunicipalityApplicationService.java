package finki.ukim.backend.administration.service.application;

import finki.ukim.backend.administration.model.dto.CreateMunicipalityDto;
import finki.ukim.backend.administration.model.dto.DisplayMunicipalityDto;

import java.util.List;
import java.util.Optional;

public interface MunicipalityApplicationService {
    List<DisplayMunicipalityDto> findAll();

    Optional<DisplayMunicipalityDto> findById(Long id);

    Optional<DisplayMunicipalityDto> findByName(String name);

    Optional<DisplayMunicipalityDto> findByCode(String code);


    DisplayMunicipalityDto create(CreateMunicipalityDto createMunicipalityDto);

    Optional<DisplayMunicipalityDto> update(Long id, CreateMunicipalityDto createMunicipalityDto);

    Optional<DisplayMunicipalityDto> deleteById(Long id);
}
