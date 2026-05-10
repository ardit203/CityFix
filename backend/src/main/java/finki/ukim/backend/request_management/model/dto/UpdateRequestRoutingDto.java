package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.enums.Priority;

public record UpdateRequestRoutingDto(
        Long categoryId,
        Long departmentId,
        Long municipalityId,
        Priority priority,
        String summary,
        String note
) {
}
