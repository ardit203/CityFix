package finki.ukim.backend.administration.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.model.dto.filters.MunicipalityFilterDto;
import finki.ukim.backend.administration.model.exception.MunicipalityNotFoundException;
import finki.ukim.backend.administration.repository.MunicipalityRepository;
import finki.ukim.backend.administration.service.domain.MunicipalityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MunicipalityServiceImpl implements MunicipalityService {
    private final MunicipalityRepository municipalityRepository;


    @Override
    public List<Municipality> findAll() {
        return municipalityRepository.findAll();
    }

    @Override
    public Municipality findById(Long id) {
        return municipalityRepository.findById(id)
                .orElseThrow(() -> new MunicipalityNotFoundException(id));
    }

    @Override
    public Municipality findByCode(String code) {
        return municipalityRepository.findByCode(code)
                .orElseThrow(() -> new MunicipalityNotFoundException(code));
    }

    @Override
    public Municipality create(Municipality municipality) {
        return municipalityRepository.save(municipality);
    }

    @Override
    public Municipality update(Long id, Municipality municipality) {
        Municipality existingMunicipality = findById(id);
        if (municipality.getName() != null) {
            existingMunicipality.setName(municipality.getName());
        }
        if (municipality.getCode() != null) {
            existingMunicipality.setCode(municipality.getCode());
        }
        return municipalityRepository.save(existingMunicipality);
    }

    @Override
    public Municipality deleteById(Long id) {
        Municipality municipality = findById(id);
        municipalityRepository.delete(municipality);
        return municipality;
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        municipalityRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public Page<Municipality> findAll(MunicipalityFilterDto municipalityFilterDto) {
        municipalityFilterDto.normalizeTextFields();
        Pageable pageable = municipalityFilterDto.toPageable();
        return municipalityRepository.findFiltered(
                municipalityFilterDto.getId(),
                municipalityFilterDto.getCode(),
                municipalityFilterDto.getName(),
                pageable
        );
    }
}
