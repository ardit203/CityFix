package finki.ukim.backend.administration.service.domain;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
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

    Page<Department> findAll(int page, int size, String sortBy, Long id, String text);
}
