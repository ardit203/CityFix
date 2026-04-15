package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class EmailAlreadyExistsException extends ConflictException {
    public EmailAlreadyExistsException(String email) {
        super(String.format("User with email '%s' already exists", email));
    }
}
