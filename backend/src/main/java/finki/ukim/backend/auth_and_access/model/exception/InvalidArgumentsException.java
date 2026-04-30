package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class InvalidArgumentsException extends ConflictException {
    public InvalidArgumentsException() {
        super("Required fields are missing");
    }
}
