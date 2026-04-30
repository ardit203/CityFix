package finki.ukim.backend.administration.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.exception.CategoryNotFoundException;
import finki.ukim.backend.administration.model.projection.CategoryPageableProjection;
import finki.ukim.backend.administration.repository.CategoryRepository;
import finki.ukim.backend.administration.service.domain.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findByIdWithDepartment(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
    }

    @Override
    public List<Category> findByDepartmentId(Long departmentId) {
        return categoryRepository.findByDepartmentId(departmentId);
    }

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category category) {
        Category existingCategory = findById(id);

        if (category.getName() != null) {
            existingCategory.setName(category.getName());
        }
        if (category.getDescription() != null) {
            existingCategory.setDescription(category.getDescription());
        }
        if (category.getDepartment() != null) {
            existingCategory.setDepartment(category.getDepartment());
        }
        return categoryRepository.save(existingCategory);
    }

    @Override
    public Category deleteById(Long id) {
        Category category = findById(id);
        categoryRepository.delete(category);
        return category;
    }

    @Override
    public Page<CategoryPageableProjection> findAll(int page, int size, String sortBy, Long id, String text, Long departmentId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).and(Sort.by("createdAt")));
        return categoryRepository.findFiltered(id, text, departmentId, pageable);
    }
}
