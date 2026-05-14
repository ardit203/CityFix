package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import finki.ukim.backend.request_management.model.projection.RequestAssignmentPageableProjection;

import java.time.LocalDateTime;

public record DisplayRequestAssignmentPageableDto(
        Long id,
        Long requestId,
        String requestTitle,
        Priority requestPriority,
        RequestStatus requestStatus,
        String departmentName,
        Long employeeId,
        String employeeUsername,
        String employeeName,
        String employeeSurname,
        Long assignedByUserId,
        String assignedByUsername,
        String assignedByName,
        String assignedBySurname,
        LocalDateTime assignedAt
) {
    public static DisplayRequestAssignmentPageableDto from(RequestAssignmentPageableProjection projection) {
        return new DisplayRequestAssignmentPageableDto(
                projection.getId(),
                projection.getRequestId(),
                projection.getRequestTitle(),
                projection.getRequestPriority(),
                projection.getRequestStatus(),
                projection.getDepartmentName(),
                projection.getEmployeeId(),
                projection.getEmployeeUsername(),
                projection.getEmployeeName(),
                projection.getEmployeeSurname(),
                projection.getAssignedByUserId(),
                projection.getAssignedByUsername(),
                projection.getAssignedByName(),
                projection.getAssignedBySurname(),
                projection.getAssignedAt()
        );
    }
}
