package finki.ukim.backend.administration.repository;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.projection.CategoryPageableProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);

    @Query("""
                select d from Department d
                where (:id is null or d.id = :id)
                  and (
                      :text is null
                      or lower(d.name) like lower(concat('%', :text, '%'))
                      or lower(d.description) like lower(concat('%', :text, '%'))
                  )
            """)
    Page<Department> findFiltered(
            @Param("id") Long id,
            @Param("text") String text,
            Pageable pageable
    );
}
