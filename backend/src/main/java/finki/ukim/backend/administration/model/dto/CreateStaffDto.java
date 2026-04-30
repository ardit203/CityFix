package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.auth_and_access.model.domain.User;

public record CreateStaffDto(
        Long userId,
        Long departmentId,
        Long municipalityId

) {
    public Staff toStaff(User user, Department department, Municipality municipality) {
        return new Staff(user, department, municipality);
    }
}
