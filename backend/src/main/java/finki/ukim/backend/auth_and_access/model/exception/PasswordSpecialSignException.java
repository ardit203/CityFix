package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.auth_and_access.constants.PasswordConstants;
import finki.ukim.backend.common.exception.ConflictException;

public class PasswordSpecialSignException extends ConflictException {
    public PasswordSpecialSignException() {
        super(String.format("Password must contain at least one special character: %s", PasswordConstants.ALLOWED_SPECIAL));
    }
}