package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class InvalidCurrentPasswordException extends ConflictException {
    public InvalidCurrentPasswordException() {
        super("Your password does not match the current password!");
    }
}