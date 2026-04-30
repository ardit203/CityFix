package finki.ukim.backend.administration.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.exception.DepartmentNotFoundException;
import finki.ukim.backend.administration.repository.DepartmentRepository;
import finki.ukim.backend.administration.service.domain.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Department findById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    @Override
    public Department findByName(String name) {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new DepartmentNotFoundException(name));
    }

    @Override
    public Department create(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Department update(Long id, Department department) {
        Department existingDepartment = findById(id);

        if (department.getName() != null) {
            existingDepartment.setName(department.getName());
        }
        if (department.getDescription() != null) {
            existingDepartment.setDescription(department.getDescription());
        }
        return departmentRepository.save(existingDepartment);

    }

    @Override
    public Department deleteById(Long id) {
        Department department = findById(id);
        departmentRepository.delete(department);
        return department;
    }

    @Override
    public Page<Department> findAll(int page, int size, String sortBy, Long id, String text) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).and(Sort.by("createdAt")));
        return departmentRepository.findFiltered(id, text, pageable);
    }
}
