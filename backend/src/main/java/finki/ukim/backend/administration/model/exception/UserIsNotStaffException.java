package finki.ukim.backend.administration.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class UserIsNotStaffException extends ResourceNotFoundException {
    public UserIsNotStaffException(Long userId) {
        super(String.format("Staff with user id '%s' does not exist.", userId));
    }
}
