package finki.ukim.backend.administration.repository;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query("""
            select c from Category c
            join fetch c.department d
            where c.id = :id
            """)
    Optional<Category> findByIdWithDepartment(@Param("id") Long id);

    @Query("""
            select c from Category c
            join fetch c.department d
            """)
    List<Category> findAllWithDepartment();

    @Query("""
            select c from Category c
                        join fetch c.department d
                        where d.id = :departmentId
            """)
    List<Category> findByDepartmentId(@Param("departmentId") Long departmentId);
}
