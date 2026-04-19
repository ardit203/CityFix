package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class InvalidTokenException extends ConflictException {
    public InvalidTokenException() {
        super("Token is invalid or has expired.");
    }
}
