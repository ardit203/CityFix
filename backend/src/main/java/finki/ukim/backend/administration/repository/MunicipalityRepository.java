package finki.ukim.backend.administration.repository;

import finki.ukim.backend.administration.model.domain.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {
    Optional<Municipality> findByName(String name);
    Optional<Municipality> findByCode(String code);
}
