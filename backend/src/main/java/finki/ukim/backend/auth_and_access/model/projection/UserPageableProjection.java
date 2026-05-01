package finki.ukim.backend.auth_and_access.model.projection;

import finki.ukim.backend.auth_and_access.model.enums.Role;

public interface UserPageableProjection {
    Long getId();

    String getUsername();

    Role getRole();

    String getName();

    String getSurname();
}
