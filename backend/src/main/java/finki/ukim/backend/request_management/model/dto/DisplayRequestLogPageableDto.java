package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.enums.LogAction;
import finki.ukim.backend.request_management.model.projection.RequestLogPageableProjection;
import java.time.LocalDateTime;

public record DisplayRequestLogPageableDto(
        Long id,
        LogAction action,
        String newValue,
        String oldValue,
        LocalDateTime createdAt
) {
    public static DisplayRequestLogPageableDto from(RequestLogPageableProjection projection) {
        return new DisplayRequestLogPageableDto(
                projection.getId(),
                projection.getAction(),
                projection.getNewValue(),
                projection.getOldValue(),
                projection.getCreatedAt()
        );
    }
}
