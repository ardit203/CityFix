package finki.ukim.backend.request_management.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class RequestFileNotFoundException extends ResourceNotFoundException {
    public RequestFileNotFoundException(Long id) {
        super(String.format("Request file with id '%s' was not found", id));
    }
}
