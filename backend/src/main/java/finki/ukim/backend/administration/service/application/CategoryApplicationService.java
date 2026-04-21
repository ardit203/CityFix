package finki.ukim.backend.administration.service.application;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.dto.CreateCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayCategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryApplicationService {
    List<DisplayBasicCategoryDto> findAll();

    Optional<DisplayCategoryDto> findById(Long id);

    Optional<DisplayBasicCategoryDto> findByName(String name);

    List<DisplayCategoryDto> findByDepartmentId(Long departmentId);

    DisplayCategoryDto create(CreateCategoryDto createCategoryDto);

    Optional<DisplayCategoryDto> update(Long id, CreateCategoryDto createCategoryDto);

    Optional<DisplayBasicCategoryDto> deleteById(Long id);
}
