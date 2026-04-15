package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class PasswordUpperCaseException extends ConflictException {
    public PasswordUpperCaseException(int upperCaseCount) {
        super(String.format("Password must contain at least %d uppercase letters (A-Z).", upperCaseCount));
    }
}