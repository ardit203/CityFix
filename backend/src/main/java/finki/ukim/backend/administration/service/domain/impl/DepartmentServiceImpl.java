package finki.ukim.backend.administration.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.repository.DepartmentRepository;
import finki.ukim.backend.administration.service.domain.DepartmentService;
import lombok.AllArgsConstructor;
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
    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Optional<Department> findByName(String name) {
        return departmentRepository.findByName(name);
    }

    @Override
    public Department create(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Optional<Department> update(Long id, Department department) {
        return findById(id)
                .map(existingDepartment -> {
                    existingDepartment.setName(department.getName());
                    existingDepartment.setDescription(department.getDescription());
                    return departmentRepository.save(existingDepartment);
                });
    }

    @Override
    public Optional<Department> deleteById(Long id) {
        Optional<Department> department = findById(id);
        department.ifPresent(departmentRepository::delete);
        return department;
    }
}
