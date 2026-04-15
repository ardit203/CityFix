package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class PasswordLengthExceededException extends ConflictException {
    public PasswordLengthExceededException(int length) {
        super(String.format("Password cannot contain more than %d letters", length));
    }
}