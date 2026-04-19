package finki.ukim.backend.auth_and_access.service.domain.impl;

import finki.ukim.backend.auth_and_access.constants.PasswordConstants;
import finki.ukim.backend.auth_and_access.helper.PasswordHelper;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.exception.*;
import finki.ukim.backend.auth_and_access.repository.UserRepository;
import finki.ukim.backend.auth_and_access.service.domain.PasswordService;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private static final int MAX_FAILED_ATTEMPTS = 5;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User register(User user, String confirmPassword) {
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
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (user.isLocked()) {
            throw new AccountLockedException(username, user.getLockedUntil());
        }

        if (!passwordService.matches(password, user.getPassword())) {
            handleFailedLogin(user);
            throw new InvalidUserCredentialsException();
        }
        return handleSuccessfulLogin(user);
    }

    @Override
    @Transactional
    public Optional<User> update(Long id, String username, String email, Role role, Boolean notificationsEnabled) {
        return findById(id).map(existing -> {
            if (userRepository.existsByUsername(username)) {
                throw new UsernameAlreadyExistsException(username);
            }
            if (userRepository.existsByEmail(email)) {
                throw new EmailAlreadyExistsException(email);
            }
            existing.setUsername(username);
            existing.setEmail(email);
            existing.setRole(role);
            existing.setNotificationsEnabled(notificationsEnabled);
            return userRepository.save(existing);
        });
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> deleteById(Long id) {
        Optional<User> user = findById(id);
        user.ifPresent(userRepository::delete);
        return user;
    }

    @Override
    public Optional<User> deleteByUsername(String username) {
        Optional<User> user = findByUsername(username);
        user.ifPresent(userRepository::delete);
        return user;
    }

    @Override
    public Optional<User> changePassword(String username, String currentPassword, String newPassword, String confirmNewPassword) {
        return findByUsername(username).map(user -> {
            String encodedNewPassword = passwordService.preparePasswordForChange(
                    currentPassword,
                    user.getPassword(),
                    newPassword,
                    confirmNewPassword
            );

            user.setPassword(encodedNewPassword);
            return userRepository.save(user);
        });
    }

    @Override
    public void handleFailedLogin(User user) {
        int failedAttempts = user.getFailedLoginAttempts() + 1;

        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            user.setFailedLoginAttempts(0);
            user.setLockLevel(user.getLockLevel() + 1);
            user.setLockedUntil(calculateLockedUntil(user.getLockLevel()));
        } else {
            user.setFailedLoginAttempts(failedAttempts);
        }

        userRepository.save(user);
    }

    @Override
    public User handleSuccessfulLogin(User user) {
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        user.setLockLevel(0);
        return userRepository.save(user);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
