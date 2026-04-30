package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.projection.StaffPageableProjection;

public record DisplayStaffPageableDto(
        Long id,
        String name,
        String surname,
        String username,
        String email,
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
                projection.getDepartmentName(),
                projection.getMunicipalityName(),
                projection.getMunicipalityCode()
        );
    }
}
