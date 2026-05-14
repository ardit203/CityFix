package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.domain.RequestAssignment;

import java.time.LocalDateTime;
import java.util.List;

public record DisplayRequestAssignmentBasicDto(
        Long id,
        Long requestId,
        Long employeeId,
        String employeeName,
        String employeeSurname,
        String employeeUsername,
        Long assignedById,
        String assignedByName,
        String assignedBySurname,
        String assignedByUsername,
        LocalDateTime assignedAt
) {
    public static DisplayRequestAssignmentBasicDto from(RequestAssignment assignment) {
        return new DisplayRequestAssignmentBasicDto(
                assignment.getId(),
                assignment.getRequest().getId(),
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

    public static List<DisplayRequestAssignmentBasicDto> from(List<RequestAssignment> assignments) {
        return assignments.stream().map(DisplayRequestAssignmentBasicDto::from).toList();
    }
}
