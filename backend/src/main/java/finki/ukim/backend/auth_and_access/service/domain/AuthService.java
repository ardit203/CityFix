package finki.ukim.backend.auth_and_access.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;

public interface AuthService {
    User register(User user, String confirmPassword);

    User login(String username, String password);

    void forgotPassword(String email);

    void resetPassword(String token, String newPassword, String confirmPassword);
}
