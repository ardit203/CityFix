package finki.ukim.backend.administration.service.domain;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.model.dto.filters.DepartmentFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<Department> findAll();

    Department findById(Long id);

    Department findByName(String name);


    Department create(Department department);

    Department update(Long id, Department department);

    Department deleteById(Long id);

    void deleteAllById(List<Long> ids);

    Page<Department> findAll(DepartmentFilterDto departmentFilterDto);
}
