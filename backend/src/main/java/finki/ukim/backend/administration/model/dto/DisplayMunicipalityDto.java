package finki.ukim.backend.administration.model.dto;

import finki.ukim.backend.administration.model.domain.Municipality;

import java.util.List;

public record DisplayMunicipalityDto(
        Long id,
        String name,
        String code
) {
    public static DisplayMunicipalityDto from(Municipality municipality) {
        return new DisplayMunicipalityDto(
                municipality.getId(),
                municipality.getName(),
                municipality.getCode()
        );
    }

    public static List<DisplayMunicipalityDto> from(List<Municipality> municipalities) {
        return municipalities.stream()
                .map(DisplayMunicipalityDto::from)
                .toList();
    }
}
