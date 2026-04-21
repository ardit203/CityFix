package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Staff;

import java.util.List;

public record DisplayBasicStaffDto(
        Long id,
        Long userId,
        Long departmentId,
        Long municipalityId
) {
    public static DisplayBasicStaffDto from(Staff staff) {
        return new DisplayBasicStaffDto(
                staff.getId(),
                staff.getUser().getId(),
                staff.getDepartment().getId(),
                staff.getMunicipality().getId()
        );
    }

    public static List<DisplayBasicStaffDto> from(List<Staff> staffList) {
        return staffList.stream()
                .map(DisplayBasicStaffDto::from)
                .toList();
    }
}
