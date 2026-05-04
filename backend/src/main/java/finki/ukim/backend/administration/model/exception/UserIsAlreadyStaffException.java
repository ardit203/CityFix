package finki.ukim.backend.administration.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class UserIsAlreadyStaffException extends ConflictException {
    public UserIsAlreadyStaffException(Long userId) {
        super(String.format("User with id '%s' is already staff", userId));
    }
}
