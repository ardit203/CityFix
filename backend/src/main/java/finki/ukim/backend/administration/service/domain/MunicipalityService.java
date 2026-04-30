package finki.ukim.backend.administration.service.domain;

import finki.ukim.backend.administration.model.domain.Municipality;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface MunicipalityService {
    List<Municipality> findAll();

    Municipality findById(Long id);

    Municipality findByCode(String code);

    Municipality create(Municipality municipality);

    Municipality update(Long id, Municipality municipality);

    Municipality deleteById(Long id);

    Page<Municipality> findAll(int page, int size, String sortBy, Long id, String code, String name);
}
