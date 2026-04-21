package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Category;

import java.util.List;

public record DisplayCategoryDto(
        Long id,
        String name,
        String description,
        DisplayDepartmentDto department
) {
    public static DisplayCategoryDto from(Category category) {
        return new DisplayCategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                DisplayDepartmentDto.from(category.getDepartment())
        );
    }

    public static List<DisplayCategoryDto> from(List<Category> categories) {
        return categories.stream()
                .map(DisplayCategoryDto::from)
                .toList();
    }
}
