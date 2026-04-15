package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class PasswordLengthException extends ConflictException {
    public PasswordLengthException(int length) {
        super(String.format("Password must be at least %d characters long.", length));
    }
}