package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class TokenNotFoundException extends ResourceNotFoundException {
    public TokenNotFoundException() {
        super("Token not found");
    }
}
