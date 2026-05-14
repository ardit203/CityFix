package finki.ukim.backend.auth_and_access.model.dto.accessScope;

public record StaffScope(
        boolean admin,
        Long staffId,
        Long departmentId,
        Long municipalityId,
        Long userId
) {
    public static StaffScope toAdminScope() {
        return new StaffScope(
                true,
                null,
                null,
                null,
                null
        );
    }

    public static StaffScope toStaffScope(
            Long staffId,
            Long departmentId,
            Long municipalityId,
            Long userId
    ) {
        return new StaffScope(
                false,
                staffId,
                departmentId,
                municipalityId,
                userId
        );
    }
}