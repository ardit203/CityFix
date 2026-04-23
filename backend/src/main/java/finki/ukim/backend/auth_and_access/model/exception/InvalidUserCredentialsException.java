package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.BadRequestException;

public class InvalidUserCredentialsException extends BadRequestException {
    public InvalidUserCredentialsException() {
        super("Incorrect username or password!");
    }
}