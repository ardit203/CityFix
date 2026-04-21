package finki.ukim.backend.administration.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class MunicipalityNotFoundException extends ResourceNotFoundException {
    public MunicipalityNotFoundException(Long id) {
        super(String.format("Municipality with id %d not found", id));
    }
}
