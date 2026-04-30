package finki.ukim.backend.administration.service.application.impl;

import finki.ukim.backend.administration.model.dto.CreateDepartmentDto;
import finki.ukim.backend.administration.model.dto.DisplayDepartmentDto;
import finki.ukim.backend.administration.service.application.DepartmentApplicationService;
import finki.ukim.backend.administration.service.domain.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
    public DisplayDepartmentDto findById(Long id) {
        return DisplayDepartmentDto.from(departmentService.findById(id));
    }

    @Override
    public DisplayDepartmentDto findByName(String name) {
        return DisplayDepartmentDto.from(departmentService.findByName(name));
    }

    @Override
    public DisplayDepartmentDto create(CreateDepartmentDto createDepartmentDto) {
        return DisplayDepartmentDto.from(departmentService.create(createDepartmentDto.toDepartment()));
    }

    @Override
    public DisplayDepartmentDto update(Long id, CreateDepartmentDto createDepartmentDto) {
        return DisplayDepartmentDto.from(departmentService.update(id, createDepartmentDto.toDepartment()));
    }

    @Override
    public DisplayDepartmentDto deleteById(Long id) {
        return DisplayDepartmentDto.from(departmentService.deleteById(id));
    }

    @Override
    public Page<DisplayDepartmentDto> findAll(int page, int size, String sortBy, Long id, String text) {
        return departmentService
                .findAll(page, size, sortBy, id, text)
                .map(DisplayDepartmentDto::from);
    }
}
