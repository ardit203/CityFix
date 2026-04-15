package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class PasswordsDoNotMatchException extends ConflictException {
    public PasswordsDoNotMatchException() {
        super("Passwords do not match!");
    }
}