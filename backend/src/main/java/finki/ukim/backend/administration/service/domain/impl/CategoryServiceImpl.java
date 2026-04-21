package finki.ukim.backend.administration.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.repository.CategoryRepository;
import finki.ukim.backend.administration.service.domain.CategoryService;
import lombok.AllArgsConstructor;
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
    public Optional<Category> findById(Long id) {
        return categoryRepository.findByIdWithDepartment(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
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
    public Optional<Category> update(Long id, Category category) {
        return findById(id)
                .map((existingCategory) -> {
                    existingCategory.setName(category.getName());
                    existingCategory.setDescription(category.getDescription());
                    existingCategory.setDepartment(category.getDepartment());
                    return categoryRepository.save(existingCategory);
                });
    }

    @Override
    public Optional<Category> deleteById(Long id) {
        Optional<Category> category = findById(id);
        category.ifPresent(categoryRepository::delete);
        return category;
    }
}
