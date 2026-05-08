package finki.ukim.backend.auth_and_access.model.dto.accessScope;

public record StaffScopeFilters(
        Long departmentId,
        Long municipalityId
) {}