package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.enums.RequestStatus;

import java.util.List;

public record DisplayBasicRequestDto(
        Long id,
        String title,
        RequestStatus status,
        Long userId,
        Long categoryId,
        Long municipalityId,
        Long departmentId
) {
    public static DisplayBasicRequestDto from(Request request) {
        return new DisplayBasicRequestDto(
                request.getId(),
                request.getTitle(),
                request.getStatus(),
                request.getUser() == null ? null : request.getUser().getId(),
                request.getCategory() == null ? null : request.getCategory().getId(),
                request.getDepartment() == null ? null : request.getDepartment().getId(),
                request.getMunicipality() == null ? null : request.getMunicipality().getId()
        );
    }

    public static List<DisplayBasicRequestDto> from(List<Request> requests) {
        return requests.stream()
                .map(DisplayBasicRequestDto::from)
                .toList();
    }
}
