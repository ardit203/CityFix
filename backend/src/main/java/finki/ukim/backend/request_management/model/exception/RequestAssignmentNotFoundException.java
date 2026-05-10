package finki.ukim.backend.request_management.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class RequestAssignmentNotFoundException extends ResourceNotFoundException {
    public RequestAssignmentNotFoundException(Long id) {
        super(String.format("RequestAssignment with id '%s' was not found", id));
    }
}
