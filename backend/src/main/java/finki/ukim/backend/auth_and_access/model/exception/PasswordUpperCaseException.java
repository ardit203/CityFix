package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.auth_and_access.constants.PasswordConstants;
import finki.ukim.backend.common.exception.ConflictException;

public class PasswordUpperCaseException extends ConflictException {
    public PasswordUpperCaseException() {
        super(String.format("Password must contain at least %d uppercase letters (A-Z).", PasswordConstants.UPPERCASE_COUNT));
    }
}