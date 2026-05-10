package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import finki.ukim.backend.request_management.model.projection.RequestAssignmentPageableProjection;

import java.time.LocalDateTime;

public record DisplayRequestAssignmentPageableDto(
        Long id,
        String getRequestTitle,
        Priority requestPriority,
        RequestStatus requestStatus,
        String employeeUsername,
        String assignedByUsername,
        LocalDateTime assignedAt
) {
    public static DisplayRequestAssignmentPageableDto from(RequestAssignmentPageableProjection projection) {
        return new DisplayRequestAssignmentPageableDto(
                projection.getId(),
                projection.getRequestTitle(),
                projection.getRequestPriority(),
                projection.getRequestStatus(),
                projection.getEmployeeUsername(),
                projection.getAssignedByUsername(),
                projection.getAssignedAt()
        );
    }
}
