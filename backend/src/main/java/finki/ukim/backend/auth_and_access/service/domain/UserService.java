package finki.ukim.backend.auth_and_access.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    User register(User user, String confirmPassword);

    User login(String username, String password);

    Optional<User> update(Long id, String username, String email, Role role, Boolean notificationsEnabled);

    User save(User user);

    Optional<User> deleteById(Long id);

    Optional<User> deleteByUsername(String username);

    Optional<User> changePassword(String username, String currentPassword, String newPassword, String confirmNewPassword);

    void handleFailedLogin(User user);

    User handleSuccessfulLogin(User user);

    LocalDateTime calculateLockedUntil(int lockLevel);
}
