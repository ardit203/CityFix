package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import finki.ukim.backend.request_management.model.enums.RoutingStatus;

public record DisplayRequestDto(
        Long id,
        String title,
        String description,
        RequestLocationDto location,
        String summary,
        Priority priority,
        RequestStatus status,
        RoutingStatus routingStatus,
        Long userId,
        Long categoryId,
        Long departmentId,
        Long municipalityId
) {
    public static DisplayRequestDto from(Request request) {
        return new DisplayRequestDto(
                request.getId(),
                request.getTitle(),
                request.getDescription(),
                RequestLocationDto.from(request.getLocation()),
                request.getSummary(),
                request.getPriority(),
                request.getStatus(),
                request.getRoutingStatus(),
                request.getUser() == null ? null : request.getUser().getId(),
                request.getCategory() == null ? null : request.getCategory().getId(),
                request.getDepartment() == null ? null : request.getDepartment().getId(),
                request.getMunicipality() == null ? null : request.getMunicipality().getId()
        );
    }
}
