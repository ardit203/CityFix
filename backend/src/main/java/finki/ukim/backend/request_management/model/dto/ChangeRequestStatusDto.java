package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.enums.RequestStatus;

public record ChangeRequestStatusDto(
        RequestStatus status,
        String reason,
        String note
) {
}