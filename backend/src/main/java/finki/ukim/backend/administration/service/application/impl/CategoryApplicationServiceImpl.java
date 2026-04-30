package finki.ukim.backend.administration.service.application.impl;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.dto.CreateCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayCategoryPageableDto;
import finki.ukim.backend.administration.model.exception.DepartmentNotFoundException;
import finki.ukim.backend.administration.service.application.CategoryApplicationService;
import finki.ukim.backend.administration.service.domain.CategoryService;
import finki.ukim.backend.administration.service.domain.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
    public DisplayCategoryDto findById(Long id) {
        return DisplayCategoryDto.from(categoryService.findById(id));
    }

    @Override
    public DisplayBasicCategoryDto findByName(String name) {
        return DisplayBasicCategoryDto.from(categoryService.findByName(name));
    }

    @Override
    public List<DisplayCategoryDto> findByDepartmentId(Long departmentId) {
        return DisplayCategoryDto.from(categoryService.findByDepartmentId(departmentId));
    }

    @Override
    public DisplayCategoryDto create(CreateCategoryDto createCategoryDto) {
        Department department = departmentService.findById(createCategoryDto.departmentId());
        return DisplayCategoryDto.from(categoryService.create(createCategoryDto.toCategory(department)));
    }

    @Override
    public DisplayCategoryDto update(Long id, CreateCategoryDto createCategoryDto) {
        Department department = departmentService.findById(createCategoryDto.departmentId());
        return DisplayCategoryDto.from(
                categoryService.update(id, createCategoryDto.toCategory(department))
        );
    }

    @Override
    public DisplayBasicCategoryDto deleteById(Long id) {
        return DisplayBasicCategoryDto.from(categoryService.deleteById(id));
    }

    @Override
    public Page<DisplayCategoryPageableDto> findAll(int page, int size, String sortBy, Long id, String text, Long departmentId) {
        return categoryService
                .findAll(page, size, sortBy, id, text, departmentId)
                .map(DisplayCategoryPageableDto::from);
    }
}
