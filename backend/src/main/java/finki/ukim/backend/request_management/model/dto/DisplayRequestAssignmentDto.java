package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.domain.RequestAssignment;
import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;

import java.time.LocalDateTime;

public record DisplayRequestAssignmentDto(
        Long id,
        Long requestId,
        String requestTitle,
        Priority requestPriority,
        RequestStatus requestStatus,
        Long departmentId,
        String departmentName,
        Long employeeId,
        String employeeName,
        String employeeSurname,
        String employeeUsername,
        Long assignedByUserId,
        String assignedByName,
        String assignedBySurname,
        String assignedByUsername,
        LocalDateTime assignedAt
) {
    public static DisplayRequestAssignmentDto from(RequestAssignment assignment) {
        return new DisplayRequestAssignmentDto(
                assignment.getId(),
                assignment.getRequest().getId(),
                assignment.getRequest().getTitle(),
                assignment.getRequest().getPriority(),
                assignment.getRequest().getStatus(),
                assignment.getRequest().getDepartment() == null ? null : assignment.getRequest().getDepartment().getId(),
                assignment.getRequest().getDepartment() == null ? null : assignment.getRequest().getDepartment().getName(),
                assignment.getEmployee().getId(),
                assignment.getEmployee().getProfile().getName(),
                assignment.getEmployee().getProfile().getSurname(),
                assignment.getEmployee().getUsername(),
                assignment.getAssignedBy().getId(),
                assignment.getAssignedBy().getProfile().getName(),
                assignment.getAssignedBy().getProfile().getSurname(),
                assignment.getAssignedBy().getUsername(),
                assignment.getAssignedAt()
        );
    }
}
