package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.domain.RequestLog;
import finki.ukim.backend.request_management.model.enums.LogAction;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record DisplayRequestLogBasicDto(
        Long id,
        LogAction action,
        LocalDateTime createdAt
) {
    public static DisplayRequestLogBasicDto from(RequestLog requestLog) {
        return new DisplayRequestLogBasicDto(
                requestLog.getId(),
                requestLog.getAction(),
                requestLog.getCreatedAt()
        );
    }

    public static List<DisplayRequestLogBasicDto> from(List<RequestLog> requestLogs) {
        return requestLogs
                .stream()
                .map(DisplayRequestLogBasicDto::from)
                .toList();
    }
}
