package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.projection.StaffPageableProjection;
import finki.ukim.backend.auth_and_access.model.enums.Role;

public record DisplayStaffPageableDto(
        Long id,
        String name,
        String surname,
        String username,
        String email,
        Role role,
        String departmentName,
        String municipalityName,
        String municipalityCode
) {
    public static DisplayStaffPageableDto from(StaffPageableProjection projection) {
        return new DisplayStaffPageableDto(
                projection.getId(),
                projection.getName(),
                projection.getSurname(),
                projection.getUsername(),
                projection.getEmail(),
                projection.getRole(),
                projection.getDepartmentName(),
                projection.getMunicipalityName(),
                projection.getMunicipalityCode()
        );
    }
}
