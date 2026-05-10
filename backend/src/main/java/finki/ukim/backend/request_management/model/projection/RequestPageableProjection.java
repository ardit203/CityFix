package finki.ukim.backend.request_management.model.projection;

import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import finki.ukim.backend.request_management.model.enums.RoutingStatus;

public interface RequestPageableProjection {
    Long getId();

    String getTitle();

    Priority getPriority();

    RequestStatus getStatus();

    RoutingStatus getRoutingStatus();

    String getMunicipalityCode();

    String getCategoryName();

    String getDepartmentName();
}
