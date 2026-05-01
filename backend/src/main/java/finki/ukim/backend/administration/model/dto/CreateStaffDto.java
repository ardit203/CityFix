package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.auth_and_access.model.domain.User;

import jakarta.validation.constraints.NotNull;

public record CreateStaffDto(
        @NotNull(message = "User ID is required")
        Long userId,
        
        @NotNull(message = "Department ID is required")
        Long departmentId,
        
        @NotNull(message = "Municipality ID is required")
        Long municipalityId

) {
    public Staff toStaff(User user, Department department, Municipality municipality) {
        return new Staff(user, department, municipality);
    }
}
