package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class UsernameAlreadyExistsException extends ConflictException {

    public UsernameAlreadyExistsException(String username) {
        super(String.format("User with username '%s' already exists", username));
    }
}
