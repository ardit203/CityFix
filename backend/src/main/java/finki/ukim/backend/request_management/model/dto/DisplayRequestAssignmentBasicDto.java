package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.domain.RequestAssignment;

public record DisplayRequestAssignmentBasicDto(
        Long id,
        Long requestId,
        Long employeeId,
        Long assignedById
) {
    public static DisplayRequestAssignmentBasicDto from(RequestAssignment assignment) {
        return new DisplayRequestAssignmentBasicDto(
                assignment.getId(),
                assignment.getRequest().getId(),
                assignment.getEmployee().getId(),
                assignment.getAssignedBy().getId()
        );
    }
}
