package finki.ukim.backend.auth_and_access.service.domain.impl;

import finki.ukim.backend.auth_and_access.constants.AuthConstants;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.service.domain.AccountLockService;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class AccountLockServiceImpl implements AccountLockService {
    private final UserService userService;

    @Override
    public void handleFailedLogin(User user) {
        int failedAttempts = user.getFailedLoginAttempts() + 1;

        if (failedAttempts >= AuthConstants.MAX_FAILED_ATTEMPTS) {
            user.setFailedLoginAttempts(0);
            user.setLockLevel(user.getLockLevel() + 1);
            user.setLockedUntil(calculateLockedUntil(user.getLockLevel()));
        } else {
            user.setFailedLoginAttempts(failedAttempts);
        }

        userService.save(user);
    }

    @Override
    public User handleSuccessfulLogin(User user) {
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        user.setLockLevel(0);
        return userService.save(user);
    }

    @Override
    public LocalDateTime calculateLockedUntil(int lockLevel) {
        return switch (lockLevel) {
            case 1 -> LocalDateTime.now().plusMinutes(5);
            case 2 -> LocalDateTime.now().plusMinutes(30);
            case 3 -> LocalDateTime.now().plusHours(2);
            case 4 -> LocalDateTime.now().plusHours(24);
            default -> LocalDateTime.now().plusDays(30);
        };
    }
}
