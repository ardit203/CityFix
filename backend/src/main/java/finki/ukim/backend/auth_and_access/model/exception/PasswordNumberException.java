package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class PasswordNumberException extends ConflictException {
    public PasswordNumberException(int numberCount) {
        super(String.format("Password must contain at least %d numbers (0-9).", numberCount));
    }
}