package finki.ukim.backend.auth_and_access.service.domain.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.exception.*;
import finki.ukim.backend.auth_and_access.repository.UserRepository;
import finki.ukim.backend.auth_and_access.service.domain.AccountLockService;
import finki.ukim.backend.auth_and_access.service.domain.AuthService;
import finki.ukim.backend.auth_and_access.service.domain.PasswordResetService;
import finki.ukim.backend.auth_and_access.service.domain.PasswordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final AccountLockService accountLockService;
    private final PasswordResetService passwordResetService;

    @Override
    public User register(User user, String confirmPassword) {
        validate(user.getUsername(), user.getEmail(), user.getPassword(), confirmPassword);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }

        String encodedPassword = passwordService.preparePasswordForRegistration(
                user.getPassword(),
                confirmPassword
        );

        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        validate(username, password);
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (user.isLocked()) {
            throw new AccountLockedException(username, user.getLockedUntil());
        }

        if (!passwordService.matches(password, user.getPassword())) {
            accountLockService.handleFailedLogin(user);
            throw new InvalidUserCredentialsException();
        }
        return accountLockService.handleSuccessfulLogin(user);
    }

    @Override
    public void forgotPassword(String email) {
        passwordResetService.requestPasswordReset(email);
    }

    @Override
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        passwordResetService.resetPassword(token, newPassword, confirmPassword);
    }

    private void validate(String... args) {
        for (String arg : args) {
            if (arg == null || arg.isEmpty()) {
                throw new InvalidArgumentsException();
            }
        }
    }
}
