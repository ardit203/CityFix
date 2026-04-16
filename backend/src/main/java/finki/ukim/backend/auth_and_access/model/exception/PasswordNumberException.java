package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.auth_and_access.constants.PasswordConstants;
import finki.ukim.backend.common.exception.ConflictException;

public class PasswordNumberException extends ConflictException {
    public PasswordNumberException() {
        super(String.format("Password must contain at least %d numbers (0-9).", PasswordConstants.NUMBER_COUNT));
    }
}