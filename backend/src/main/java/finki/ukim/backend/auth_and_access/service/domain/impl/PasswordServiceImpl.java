package finki.ukim.backend.auth_and_access.service.domain.impl;

import finki.ukim.backend.auth_and_access.constants.PasswordConstants;
import finki.ukim.backend.auth_and_access.helper.PasswordHelper;
import finki.ukim.backend.auth_and_access.model.exception.*;
import finki.ukim.backend.auth_and_access.service.domain.PasswordService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final PasswordEncoder passwordEncoder;
    private final PasswordHelper passwordHelper;

    @Override
    public String preparePasswordForRegistration(String rawPassword, String confirmPassword) {
        validateRequired(rawPassword, confirmPassword);
        validatePasswordsMatch(rawPassword, confirmPassword);
        validatePasswordPolicy(rawPassword);
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public String preparePasswordForChange(
            String currentPassword,
            String storedEncodedPassword,
            String newPassword,
            String confirmNewPassword
    ) {
        validateRequired(currentPassword, newPassword, confirmNewPassword);

        if (!passwordEncoder.matches(currentPassword, storedEncodedPassword)) {
            throw new InvalidCurrentPasswordException();
        }

        validatePasswordsMatch(newPassword, confirmNewPassword);

        if (passwordEncoder.matches(newPassword, storedEncodedPassword)) {
            throw new NewPasswordCannotBeSameAsOldException();
        }

        validatePasswordPolicy(newPassword);

        return passwordEncoder.encode(newPassword);
    }

    @Override
    public void validatePasswordPolicy(String password) {
        if (password.length() < PasswordConstants.MIN_PASSWORD_LENGTH) {
            throw new PasswordLengthException();
        }

        if (password.length() > PasswordConstants.MAX_PASSWORD_LENGTH) {
            throw new PasswordLengthExceededException();
        }

        if (!passwordHelper.containsOnlyAllowedSpecialSigns(password)) {
            throw new PasswordSpecialSignNotAllowedException();
        }

        if (!passwordHelper.hasUpper(password)) {
            throw new PasswordUpperCaseException();
        }

        if (!passwordHelper.hasNumber(password)) {
            throw new PasswordNumberException();
        }

        if (!passwordHelper.hasSpecialSign(password)) {
            throw new PasswordSpecialSignException();
        }
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private void validateRequired(String... values) {
        for (String value : values) {
            if (value == null || value.isBlank()) {
                throw new PasswordCannotBeEmptyException();
            }
        }
    }

    private void validatePasswordsMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordsDoNotMatchException();
        }
    }
}