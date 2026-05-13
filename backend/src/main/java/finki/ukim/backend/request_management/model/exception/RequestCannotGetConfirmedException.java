package finki.ukim.backend.request_management.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class RequestCannotGetConfirmedException extends ConflictException {
    public RequestCannotGetConfirmedException() {
        super("Request cannot be confirmed without a category or department or priority!");
    }
}
