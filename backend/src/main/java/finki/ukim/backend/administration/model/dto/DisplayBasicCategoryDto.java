package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Category;

import java.util.List;

public record DisplayBasicCategoryDto(
        Long id,
        String name,
        String description,
        Long departmentId
) {
    public static DisplayBasicCategoryDto from(Category category) {
        return new DisplayBasicCategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getDepartment().getId()
        );
    }

    public static List<DisplayBasicCategoryDto> from(List<Category> categories) {
        return categories.stream()
                .map(DisplayBasicCategoryDto::from)
                .toList();
    }
}
