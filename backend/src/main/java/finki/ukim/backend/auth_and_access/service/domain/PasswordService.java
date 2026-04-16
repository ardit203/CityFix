package finki.ukim.backend.auth_and_access.service.domain;

public interface PasswordService {
    String preparePasswordForRegistration(String rawPassword, String confirmPassword);

    String preparePasswordForChange(
            String currentPassword,
            String storedEncodedPassword,
            String newPassword,
            String confirmNewPassword
    );

    void validatePasswordPolicy(String password);

    boolean matches(String rawPassword, String encodedPassword);
}