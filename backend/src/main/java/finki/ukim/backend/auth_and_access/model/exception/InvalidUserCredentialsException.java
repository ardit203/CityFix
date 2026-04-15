package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class InvalidUserCredentialsException extends ConflictException {
    public InvalidUserCredentialsException() {
        super("Incorrect username or password!");
    }
}