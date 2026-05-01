package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.domain.Department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryDto(
        @NotBlank(message = "Name is required")
        String name,
        
        String description,
        
        @NotNull(message = "Department ID is required")
        Long departmentId
) {
    public Category toCategory(Department department) {
        return new Category(name, description, department);
    }
}
