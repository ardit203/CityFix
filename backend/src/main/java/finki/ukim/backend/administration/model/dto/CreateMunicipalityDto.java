package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Municipality;

import jakarta.validation.constraints.NotBlank;

public record CreateMunicipalityDto(
        @NotBlank(message = "Name is required")
        String name,
        
        @NotBlank(message = "Code is required")
        String code
) {
    public Municipality toMunicipality() {
        return new Municipality(name, code);
    }
}
