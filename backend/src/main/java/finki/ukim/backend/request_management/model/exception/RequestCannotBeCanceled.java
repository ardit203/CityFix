package finki.ukim.backend.request_management.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class RequestCannotBeCanceled extends ConflictException {
    public RequestCannotBeCanceled(Long id) {
        super(String.format("Request with id '%d' cannot be canceled!", id));
    }
}
