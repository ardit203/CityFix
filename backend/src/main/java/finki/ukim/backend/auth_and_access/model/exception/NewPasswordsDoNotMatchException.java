package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class NewPasswordsDoNotMatchException extends ConflictException {
    public NewPasswordsDoNotMatchException() {
        super("Your new passwords do not match!");
    }
}