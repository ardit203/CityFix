package finki.ukim.backend.administration.service.application;

import finki.ukim.backend.administration.model.dto.CreateMunicipalityDto;
import finki.ukim.backend.administration.model.dto.DisplayMunicipalityDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface MunicipalityApplicationService {
    List<DisplayMunicipalityDto> findAll();

    DisplayMunicipalityDto findById(Long id);

    DisplayMunicipalityDto findByCode(String code);


    DisplayMunicipalityDto create(CreateMunicipalityDto createMunicipalityDto);

    DisplayMunicipalityDto update(Long id, CreateMunicipalityDto createMunicipalityDto);

    DisplayMunicipalityDto deleteById(Long id);

    Page<DisplayMunicipalityDto> findAll(int page, int size, String sortBy, Long id, String code, String name);
}
