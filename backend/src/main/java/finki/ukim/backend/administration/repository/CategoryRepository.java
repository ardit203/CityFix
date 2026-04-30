package finki.ukim.backend.administration.repository;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.projection.CategoryPageableProjection;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("""
                select c.id as id,
                       c.name as name,
                       c.department.id as departmentId,
                       c.department.name as departmentName
                from Category c
                where (:id is null or c.id = :id)
                  and (
                      :text is null
                      or lower(c.name) like lower(concat('%', :text, '%'))
                      or lower(c.description) like lower(concat('%', :text, '%'))
                  )
                  and (:departmentId is null or c.department.id = :departmentId)
            """)
    Page<CategoryPageableProjection> findFiltered(@Param("id") Long id,
                                                  @Param("text") String text,
                                                  @Param("departmentId") Long departmentId,
                                                  Pageable pageable);
}
