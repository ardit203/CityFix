package finki.ukim.backend.administration.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class StaffNotFoundException extends ResourceNotFoundException {
    public StaffNotFoundException(Long id) {
        super(String.format("Staff with id '%s' does not exist.", id));
    }

//    public StaffNotFoundException() {
//        super(String.format("Staff with id '%s' does not exist.", id));
//    }
}
