package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class NewPasswordCannotBeSameAsOldException extends ConflictException {
    public NewPasswordCannotBeSameAsOldException() {
        super("Your new password cannot be the same as the old one!");
    }
}