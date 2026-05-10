package finki.ukim.backend.request_management.model.exception;

import finki.ukim.backend.request_management.model.enums.RequestStatus;

public class InvalidRequestStatusTransitionException extends RuntimeException {

    public InvalidRequestStatusTransitionException(RequestStatus oldStatus, RequestStatus newStatus) {
        super("Invalid request status transition from " + oldStatus + " to " + newStatus + ".");
    }
}