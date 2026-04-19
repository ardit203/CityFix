package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class UserWithEmailDoesNotExistException extends ConflictException {
    public UserWithEmailDoesNotExistException(String email) {
        super(String.format("User with email '%s' does not exist", email));
    }
}
