package finki.ukim.backend.administration.repository;

import finki.ukim.backend.administration.model.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
}
