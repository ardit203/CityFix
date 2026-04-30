package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.projection.CategoryPageableProjection;

public record DisplayCategoryPageableDto(
        Long id,
        String name,
        Long departmentId,
        String departmentName
) {
    public static DisplayCategoryPageableDto from(CategoryPageableProjection projection) {
        return new DisplayCategoryPageableDto(
                projection.getId(),
                projection.getName(),
                projection.getDepartmentId(),
                projection.getDepartmentName()
        );
    }
}
