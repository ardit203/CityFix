package finki.ukim.backend.request_management.model.projection;

import finki.ukim.backend.request_management.model.enums.LogAction;

import java.time.LocalDateTime;

public interface RequestLogPageableProjection {
    Long getId();

    LogAction getAction();

    String getOldValue();

    String getNewValue();

    String getNote();

    LocalDateTime getCreatedAt();
}