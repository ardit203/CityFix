package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class PasswordSpecialSignNotAllowedException extends ConflictException {
    public PasswordSpecialSignNotAllowedException(String specialSigns) {
        super(String.format("Password contains a character that is not allowed. Allowed special characters are: %s",specialSigns));
    }
}