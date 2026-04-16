package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.auth_and_access.constants.PasswordConstants;
import finki.ukim.backend.common.exception.ConflictException;

public class PasswordLengthExceededException extends ConflictException {
    public PasswordLengthExceededException() {
        super(String.format("Password cannot contain more than %d letters", PasswordConstants.MAX_PASSWORD_LENGTH));
    }
}