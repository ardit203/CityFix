package finki.ukim.backend.service.administration;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.repository.CategoryRepository;
import finki.ukim.backend.administration.service.domain.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryServiceImpl categoryService;

    private Department dept;
    private Category category;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository);
        dept            = new Department("Roads", "Road dept");
        category        = new Category("Pothole", "Road potholes", dept);
    }

    // ── create ────────────────────────────────────────────────────────────

    @Test
    void create_shouldSaveAndReturnCategory() {
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.create(category);

        assertThat(result).isEqualTo(category);
        verify(categoryRepository).save(category);
    }

    // ── update ────────────────────────────────────────────────────────────

    @Test
    void update_shouldUpdateAndReturnCategory_whenIdExists() {
        Category updated = new Category("Pothole v2", "Updated desc", dept);

        when(categoryRepository.findByIdWithDepartment(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Category> result = categoryService.update(1L, updated);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Pothole v2");
        assertThat(result.get().getDescription()).isEqualTo("Updated desc");
    }

    @Test
    void update_shouldReturnEmpty_whenIdDoesNotExist() {
        when(categoryRepository.findByIdWithDepartment(99L)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.update(99L, category);

        assertThat(result).isEmpty();
        verify(categoryRepository, never()).save(any());
    }

    // ── deleteById ────────────────────────────────────────────────────────

    @Test
    void deleteById_shouldReturnDeletedCategory_whenExists() {
        when(categoryRepository.findByIdWithDepartment(1L)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.deleteById(1L);

        assertThat(result).isPresent();
        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteById_shouldReturnEmpty_whenNotFound() {
        when(categoryRepository.findByIdWithDepartment(99L)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.deleteById(99L);

        assertThat(result).isEmpty();
        verify(categoryRepository, never()).delete(any());
    }

    // ── findByDepartmentId ────────────────────────────────────────────────

    @Test
    void findByDepartmentId_shouldReturnMatchingCategories() {
        Category cat2 = new Category("Signage", "Missing signs", dept);
        when(categoryRepository.findByDepartmentId(1L)).thenReturn(List.of(category, cat2));

        List<Category> result = categoryService.findByDepartmentId(1L);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Category::getName)
                .containsExactlyInAnyOrder("Pothole", "Signage");
    }

    @Test
    void findByDepartmentId_shouldReturnEmptyList_whenNoMatch() {
        when(categoryRepository.findByDepartmentId(99L)).thenReturn(List.of());

        List<Category> result = categoryService.findByDepartmentId(99L);

        assertThat(result).isEmpty();
    }
}
