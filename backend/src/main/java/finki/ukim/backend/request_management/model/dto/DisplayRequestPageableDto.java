package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import finki.ukim.backend.request_management.model.enums.RoutingStatus;
import finki.ukim.backend.request_management.model.projection.RequestPageableProjection;

public record DisplayRequestPageableDto(
        Long id,
        String title,
        Priority priority,
        RequestStatus status,
        RoutingStatus routingStatus,
        String municipalityCode,
        String categoryName,
        String departmentName
) {
    public static DisplayRequestPageableDto from(RequestPageableProjection projection) {
        return new DisplayRequestPageableDto(
                projection.getId(),
                projection.getTitle(),
                projection.getPriority(),
                projection.getStatus(),
                projection.getRoutingStatus(),
                projection.getMunicipalityCode(),
                projection.getCategoryName(),
                projection.getDepartmentName()
        );
    }
}
