package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Department;

import jakarta.validation.constraints.NotBlank;

public record CreateDepartmentDto(
        @NotBlank(message = "Name is required")
        String name,
        
        String description
) {
    public Department toDepartment() {
        return new Department(name, description);
    }
}
