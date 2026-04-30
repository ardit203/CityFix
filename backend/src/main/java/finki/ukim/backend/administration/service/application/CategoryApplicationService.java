package finki.ukim.backend.administration.service.application;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.dto.CreateCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayCategoryDto;
import finki.ukim.backend.administration.model.dto.DisplayCategoryPageableDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CategoryApplicationService {
    List<DisplayBasicCategoryDto> findAll();

    DisplayCategoryDto findById(Long id);

    DisplayBasicCategoryDto findByName(String name);

    List<DisplayCategoryDto> findByDepartmentId(Long departmentId);

    DisplayCategoryDto create(CreateCategoryDto createCategoryDto);

    DisplayCategoryDto update(Long id, CreateCategoryDto createCategoryDto);

    DisplayBasicCategoryDto deleteById(Long id);

    Page<DisplayCategoryPageableDto> findAll(int page, int size, String sortBy, Long id, String text, Long departmentId);
}
