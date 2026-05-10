package finki.ukim.backend.request_management.model.exception;


import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class RequestCommentNotFoundException extends ResourceNotFoundException {
    public RequestCommentNotFoundException(Long id) {
        super(String.format("Request comment with ID %d not found.", id));
    }
}
