package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.domain.RequestAssignment;
import jakarta.validation.constraints.NotNull;

public record CreateRequestAssignmentDto(
        @NotNull
        Long employeeId
) {
    public RequestAssignment toRequestAssignment(
            Request request,
            User employee,
            User assignedBy
    ) {
        return new RequestAssignment(request, employee, assignedBy);
    }
}
