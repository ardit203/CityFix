package finki.ukim.backend.administration.service.domain;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.projection.CategoryPageableProjection;
import finki.ukim.backend.auth_and_access.model.projection.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();

    Category findById(Long id);

    Category findByName(String name);

    List<Category> findByDepartmentId(Long departmentId);

    Category create(Category category);

    Category update(Long id, Category category);

    Category deleteById(Long id);

    Page<CategoryPageableProjection> findAll(int page, int size, String sortBy, Long id, String text, Long departmentId);
}
