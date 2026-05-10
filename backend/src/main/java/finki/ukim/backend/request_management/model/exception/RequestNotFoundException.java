package finki.ukim.backend.request_management.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class RequestNotFoundException extends ResourceNotFoundException {

    public RequestNotFoundException(Long id) {
        super(String.format("Request with id '%s' was not found", id));
    }
}
