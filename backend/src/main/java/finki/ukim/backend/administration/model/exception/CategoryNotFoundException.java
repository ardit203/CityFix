package finki.ukim.backend.administration.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class CategoryNotFoundException extends ResourceNotFoundException {
    public CategoryNotFoundException(Long id) {
        super(String.format("Category with id '%s' does not exist.", id));
    }

    public CategoryNotFoundException(String name) {
        super(String.format("Category with name '%s' does not exist.", name));
    }
}
