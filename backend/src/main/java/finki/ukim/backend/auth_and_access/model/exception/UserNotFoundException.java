package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String username) {
        super(String.format("User with username '%s' does not exist.", username));
    }

    public UserNotFoundException(Long id) {
        super(String.format("User with id '%d' does not exist.", id));
    }

    public UserNotFoundException() {
        super("User with that email does not exist.");
    }
}
