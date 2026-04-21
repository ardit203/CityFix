package finki.ukim.backend.administration.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.repository.MunicipalityRepository;
import finki.ukim.backend.administration.service.domain.MunicipalityService;
import lombok.AllArgsConstructor;
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
    public Optional<Municipality> findById(Long id) {
        return municipalityRepository.findById(id);
    }

    @Override
    public Optional<Municipality> findByName(String name) {
        return municipalityRepository.findByName(name);
    }

    @Override
    public Optional<Municipality> findByCode(String code) {
        return municipalityRepository.findByCode(code);
    }

    @Override
    public Municipality create(Municipality municipality) {
        return municipalityRepository.save(municipality);
    }

    @Override
    public Optional<Municipality> update(Long id, Municipality municipality) {
        return findById(id)
                .map((existingMunicipality) -> {
                    existingMunicipality.setName(municipality.getName());
                    existingMunicipality.setCode(municipality.getCode());
                    return municipalityRepository.save(existingMunicipality);
                });
    }

    @Override
    public Optional<Municipality> deleteById(Long id) {
        Optional<Municipality> municipality = findById(id);
        municipality.ifPresent(municipalityRepository::delete);
        return municipality;
    }
}
