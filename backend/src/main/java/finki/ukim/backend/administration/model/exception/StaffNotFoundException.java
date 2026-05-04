package finki.ukim.backend.administration.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class StaffNotFoundException extends ResourceNotFoundException {
    public StaffNotFoundException(Long id) {
        super(String.format("Staff with id '%s' does not exist.", id));
    }

    public StaffNotFoundException(Long userId, Long departmentId, Long municipalityId) {
        super(String.format(
                "Staff with user id '%s' that works on department with id '%s' and municipality with id '%s' does not exist.",
                userId, departmentId, municipalityId)
        );
    }
}
