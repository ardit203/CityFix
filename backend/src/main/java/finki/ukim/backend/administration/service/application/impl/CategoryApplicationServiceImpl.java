package finki.ukim.backend.administration.service.application.impl;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.dto.CreateCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayCategoryDto;
import finki.ukim.backend.administration.model.exception.DepartmentNotFoundException;
import finki.ukim.backend.administration.service.application.CategoryApplicationService;
import finki.ukim.backend.administration.service.domain.CategoryService;
import finki.ukim.backend.administration.service.domain.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryApplicationServiceImpl implements CategoryApplicationService {
    private final CategoryService categoryService;
    private final DepartmentService departmentService;
    @Override
    public List<DisplayBasicCategoryDto> findAll() {
        return DisplayBasicCategoryDto.from(categoryService.findAll());
    }

    @Override
    public Optional<DisplayCategoryDto> findById(Long id) {
        return categoryService.findById(id).map(DisplayCategoryDto::from);
    }

    @Override
    public Optional<DisplayBasicCategoryDto> findByName(String name) {
        return categoryService.findByName(name).map(DisplayBasicCategoryDto::from);
    }

    @Override
    public List<DisplayCategoryDto> findByDepartmentId(Long departmentId) {
        return DisplayCategoryDto.from(categoryService.findByDepartmentId(departmentId));
    }

    @Override
    public DisplayCategoryDto create(CreateCategoryDto createCategoryDto) {
        Department department = departmentService.findById(createCategoryDto.departmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(createCategoryDto.departmentId()));
        return DisplayCategoryDto.from(categoryService.create(createCategoryDto.toCategory(department)));
    }

    @Override
    public Optional<DisplayCategoryDto> update(Long id, CreateCategoryDto createCategoryDto) {
        Department department = departmentService.findById(createCategoryDto.departmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(createCategoryDto.departmentId()));
        return categoryService
                .update(id, createCategoryDto.toCategory(department))
                .map(DisplayCategoryDto::from);
    }

    @Override
    public Optional<DisplayBasicCategoryDto> deleteById(Long id) {
        return categoryService.deleteById(id).map(DisplayBasicCategoryDto::from);
    }
}
