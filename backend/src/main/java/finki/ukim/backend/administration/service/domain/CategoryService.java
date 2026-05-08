package finki.ukim.backend.administration.service.domain;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.dto.filters.CategoryFilterDto;
import finki.ukim.backend.administration.model.projection.CategoryPageableProjection;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    Category findById(Long id);

    Category findByName(String name);

    List<Category> findByDepartmentId(Long departmentId);

    Category create(Category category);

    Category update(Long id, Category category);

    Category deleteById(Long id);

    Page<CategoryPageableProjection> findAll(CategoryFilterDto categoryFilterDto);
}
