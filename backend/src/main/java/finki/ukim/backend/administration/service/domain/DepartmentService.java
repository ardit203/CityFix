package finki.ukim.backend.administration.service.domain;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<Department> findAll();

    Optional<Department> findById(Long id);

    Optional<Department> findByName(String name);


    Department create(Department department);

    Optional<Department> update(Long id, Department department);

    Optional<Department> deleteById(Long id);
}
