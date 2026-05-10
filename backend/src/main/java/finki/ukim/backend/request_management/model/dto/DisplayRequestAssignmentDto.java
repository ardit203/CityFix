package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.domain.RequestAssignment;
import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import org.apache.catalina.connector.Request;

import java.time.LocalDateTime;

public record DisplayRequestAssignmentDto(
        Long id,
        String requestTitle,
        Priority requestPriority,
        RequestStatus requestStatus,
        String departmentName,
        String employeeName,
        String employeeSurname,
        String employeeUsername,
        String assignedByName,
        String assignedBySurname,
        String assignedByUsername,
        LocalDateTime assignedAt
) {
    public static DisplayRequestAssignmentDto from(RequestAssignment assignment) {
        return new DisplayRequestAssignmentDto(
                assignment.getId(),
                assignment.getRequest().getTitle(),
                assignment.getRequest().getPriority(),
                assignment.getRequest().getStatus(),
                assignment.getRequest().getDepartment().getName(),
                assignment.getEmployee().getProfile().getName(),
                assignment.getEmployee().getProfile().getName(),
                assignment.getEmployee().getUsername(),
                assignment.getAssignedBy().getProfile().getName(),
                assignment.getAssignedBy().getProfile().getName(),
                assignment.getAssignedBy().getUsername(),
                assignment.getAssignedAt()
        );
    }
}
