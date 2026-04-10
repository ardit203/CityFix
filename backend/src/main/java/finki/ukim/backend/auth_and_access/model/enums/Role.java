package finki.ukim.backend.auth_and_access.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_CITIZEN,
    ROLE_EMPLOYEE,
    ROLE_MANAGER,
    ROLE_ADMINISTRATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}