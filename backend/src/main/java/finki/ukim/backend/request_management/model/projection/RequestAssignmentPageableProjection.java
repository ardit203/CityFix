package finki.ukim.backend.request_management.model.projection;

import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;

import java.time.LocalDateTime;

public interface RequestAssignmentPageableProjection {
    Long getId();

    Long getRequestId();

    String getRequestTitle();

    Priority getRequestPriority();

    RequestStatus getRequestStatus();

    String getDepartmentName();

    Long getEmployeeId();

    String getEmployeeUsername();

    String getEmployeeName();

    String getEmployeeSurname();

    Long getAssignedByUserId();

    String getAssignedByUsername();

    String getAssignedByName();

    String getAssignedBySurname();

    LocalDateTime getAssignedAt();
}
