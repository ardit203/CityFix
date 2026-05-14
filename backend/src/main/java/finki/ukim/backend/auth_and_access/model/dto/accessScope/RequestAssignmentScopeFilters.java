package finki.ukim.backend.auth_and_access.model.dto.accessScope;

public record RequestAssignmentScopeFilters(
        Long employeeId,
        Long assignedByUserId,
        Long departmentId,
        Long municipalityId
) {
}
