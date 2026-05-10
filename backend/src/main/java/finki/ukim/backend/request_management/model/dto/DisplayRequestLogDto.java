package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.domain.RequestLog;
import finki.ukim.backend.request_management.model.enums.LogAction;

import java.time.LocalDateTime;

public record DisplayRequestLogDto(
        Long id,
        Long requestId,
        Long userId,
        LogAction action,
        String newValue,
        String oldValue,
        String note,
        LocalDateTime createdAt
) {
    public static DisplayRequestLogDto from(RequestLog requestLog) {
        return new DisplayRequestLogDto(
                requestLog.getId(),
                requestLog.getRequest() == null ? null : requestLog.getRequest().getId(),
                requestLog.getActionBy() == null ? null : requestLog.getActionBy().getId(),
                requestLog.getAction(),
                requestLog.getNewValue(),
                requestLog.getOldValue(),
                requestLog.getNote(),
                requestLog.getCreatedAt()
        );
    }
}
