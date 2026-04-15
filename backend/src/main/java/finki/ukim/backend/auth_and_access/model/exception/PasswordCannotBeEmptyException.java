package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class PasswordCannotBeEmptyException extends ConflictException {
    public PasswordCannotBeEmptyException() {
        super("Password cannot be empty!");
    }
}