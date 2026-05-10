package finki.ukim.backend.request_management.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class RequestLogNotFoundException extends ResourceNotFoundException {
    public RequestLogNotFoundException(Long id) {
        super(String.format("RequestLog with id '%s' was not found", id));
    }
}
