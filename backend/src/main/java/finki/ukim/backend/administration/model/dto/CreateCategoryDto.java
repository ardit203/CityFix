package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.domain.Department;

public record CreateCategoryDto(
        String name,
        String description,
        Long departmentId
) {
    public Category toCategory(Department department) {
        return new Category(name, description, department);
    }
}
