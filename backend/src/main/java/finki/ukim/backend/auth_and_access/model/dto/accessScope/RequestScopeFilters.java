package finki.ukim.backend.auth_and_access.model.dto.accessScope;

public record RequestScopeFilters(
        Long requestedUserId,
        Long departmentId,
        Long municipalityId,
        Long assignedEmployeeUserId
) {
}