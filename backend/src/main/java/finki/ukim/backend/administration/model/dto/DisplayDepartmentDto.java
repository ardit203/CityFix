package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Department;

import java.util.List;

public record DisplayDepartmentDto(
        Long id,
        String name,
        String description
) {
    public static DisplayDepartmentDto from(Department department) {
        return new DisplayDepartmentDto(
                department.getId(),
                department.getName(),
                department.getDescription()
        );
    }

    public static List<DisplayDepartmentDto> from(List<Department> departments) {
        return departments.stream()
                .map(DisplayDepartmentDto::from)
                .toList();
    }
}
