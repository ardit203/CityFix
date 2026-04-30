package finki.ukim.backend.administration.repository;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {
    Optional<Municipality> findByName(String name);

    Optional<Municipality> findByCode(String code);

    @Query("""
                select m from Municipality m
                where (:id is null or m.id = :id)
                  and (:code is null or m.code = :code)
                  and (:name is null or m.name = :name)
            """)
    Page<Municipality> findFiltered(
            @Param("id") Long id,
            @Param("code") String code,
            @Param("name") String name,
            Pageable pageable
    );
}
