package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class NoActiveTokenException extends ResourceNotFoundException {
    public NoActiveTokenException(Long id) {
        super(String.format("User with id '%s' does not have an active token.", id));
    }
}
