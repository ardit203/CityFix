package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.auth_and_access.model.dto.DisplayUserBasicDto;

import java.util.List;

public record DisplayStaffDto(
        Long id,
        DisplayUserBasicDto user,
        DisplayDepartmentDto department,
        DisplayMunicipalityDto municipality
) {
    public static DisplayStaffDto from(Staff staff) {
        return new DisplayStaffDto(
                staff.getId(),
                DisplayUserBasicDto.from(staff.getUser()),
                DisplayDepartmentDto.from(staff.getDepartment()),
                DisplayMunicipalityDto.from(staff.getMunicipality())
        );
    }

    public static List<DisplayStaffDto> from(List<Staff> staffList) {
        return staffList.stream()
                .map(DisplayStaffDto::from)
                .toList();
    }
}
