package finki.ukim.backend.administration.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Municipality;
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
    public Page<Municipality> findAll(int page, int size, String sortBy, Long id, String code, String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).and(Sort.by("createdAt")));
        return municipalityRepository.findFiltered(id, code, name, pageable);
    }
}
