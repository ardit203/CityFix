package finki.ukim.backend.administration.model.projection;

import finki.ukim.backend.auth_and_access.model.enums.Role;

public interface StaffPageableProjection {
    Long getId();

    String getName();

    String getSurname();

    String getEmail();

    String getUsername();

    Role getRole();

    String getDepartmentName();

    String getMunicipalityName();

    String getMunicipalityCode();
}
