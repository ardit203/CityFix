package finki.ukim.backend.administration.service.application.impl;

import finki.ukim.backend.administration.model.dto.CreateDepartmentDto;
import finki.ukim.backend.administration.model.dto.DisplayDepartmentDto;
import finki.ukim.backend.administration.service.application.DepartmentApplicationService;
import finki.ukim.backend.administration.service.domain.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentApplicationServiceImpl implements DepartmentApplicationService {
    private final DepartmentService departmentService;


    @Override
    public List<DisplayDepartmentDto> findAll() {
        return DisplayDepartmentDto.from(departmentService.findAll());
    }

    @Override
    public Optional<DisplayDepartmentDto> findById(Long id) {
        return departmentService.findById(id).map(DisplayDepartmentDto::from);
    }

    @Override
    public Optional<DisplayDepartmentDto> findByName(String name) {
        return departmentService.findByName(name).map(DisplayDepartmentDto::from);
    }

    @Override
    public DisplayDepartmentDto create(CreateDepartmentDto createDepartmentDto) {
        return DisplayDepartmentDto.from(departmentService.create(createDepartmentDto.toDepartment()));
    }

    @Override
    public Optional<DisplayDepartmentDto> update(Long id, CreateDepartmentDto createDepartmentDto) {
        return departmentService
                .update(id, createDepartmentDto.toDepartment())
                .map(DisplayDepartmentDto::from);
    }

    @Override
    public Optional<DisplayDepartmentDto> deleteById(Long id) {
        return departmentService
                .deleteById(id)
                .map(DisplayDepartmentDto::from);
    }
}
