package finki.ukim.backend.request_management.model.projection;

import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;

import java.time.LocalDateTime;

public interface RequestAssignmentPageableProjection {
    Long getId();

    String getRequestTitle();

    Priority getRequestPriority();

    RequestStatus getRequestStatus();

    String getEmployeeUsername();

    String getAssignedByUsername();

    LocalDateTime getAssignedAt();
}
