package finki.ukim.backend.administration.service.domain;

import finki.ukim.backend.administration.model.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();

    Optional<Category> findById(Long id);

    Optional<Category> findByName(String name);

    List<Category> findByDepartmentId(Long departmentId);

    Category create(Category category);

    Optional<Category> update(Long id, Category category);

    Optional<Category> deleteById(Long id);
}
