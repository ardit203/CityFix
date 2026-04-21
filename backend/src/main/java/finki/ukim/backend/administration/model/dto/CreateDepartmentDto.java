package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Department;

public record CreateDepartmentDto(
        String name,
        String description
) {
    public Department toDepartment() {
        return new Department(name, description);
    }
}
