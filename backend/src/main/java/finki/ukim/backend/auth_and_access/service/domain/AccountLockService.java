package finki.ukim.backend.auth_and_access.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;

import java.time.LocalDateTime;

public interface AccountLockService {
    void handleFailedLogin(User user);

    User handleSuccessfulLogin(User user);

    LocalDateTime calculateLockedUntil(int lockLevel);
}
