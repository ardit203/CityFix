package finki.ukim.backend.administration.service.domain;

import finki.ukim.backend.administration.model.domain.Municipality;

import java.util.List;
import java.util.Optional;

public interface MunicipalityService {
    List<Municipality> findAll();

    Optional<Municipality> findById(Long id);

    Optional<Municipality> findByName(String name);

    Optional<Municipality> findByCode(String code);


    Municipality create(Municipality municipality);

    Optional<Municipality> update(Long id, Municipality municipality);

    Optional<Municipality> deleteById(Long id);
}
