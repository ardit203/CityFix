package finki.ukim.backend.auth_and_access.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

import java.time.LocalDateTime;

public class AccountLockedException extends ConflictException {
    public AccountLockedException(String username, LocalDateTime lockUntil) {
        super(String.format("Account '%s' is locked until %s, due to multiple failed login attempts.", username, lockUntil));
    }
}
