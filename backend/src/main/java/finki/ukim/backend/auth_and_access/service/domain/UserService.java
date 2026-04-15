package finki.ukim.backend.auth_and_access.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    User register(User user);

    User login(String username, String password);

    Optional<User> update(String username, User user);

    Optional<User> changePassword(String username, String currentPassword, String newPassword, String confirmNewPassword);

}
