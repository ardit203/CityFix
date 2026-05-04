package finki.ukim.backend.administration.model.exception;

import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.common.exception.ForbiddenException;
import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class UserIsNotStaffException extends ForbiddenException {
    public UserIsNotStaffException(String username) {
        super(String.format("User with username '%s' is not a staff member.", username));
    }

    public UserIsNotStaffException(Long userId) {
        super(String.format("User with user id '%s' is not a staff member.", userId));
    }
}
