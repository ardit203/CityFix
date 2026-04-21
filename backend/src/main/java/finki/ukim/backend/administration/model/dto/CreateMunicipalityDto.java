package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Municipality;

public record CreateMunicipalityDto(
        String name,
        String code
) {
    public Municipality toMunicipality() {
        return new Municipality(name, code);
    }
}
