package finki.ukim.backend.administration.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class DepartmentNotFoundException extends ResourceNotFoundException {
    public DepartmentNotFoundException(Long id) {
        super(String.format("Department with id %d not found", id));
    }
}
