package finki.ukim.backend.auth_and_access.helper;

import finki.ukim.backend.auth_and_access.constants.PasswordConstants;
import finki.ukim.backend.auth_and_access.model.exception.*;
import org.springframework.stereotype.Component;

@Component
public class PasswordHelper {
    public boolean hasUpper(String password) {
        int count = 0;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                count++;
            }
            if (count == PasswordConstants.UPPERCASE_COUNT) return true;
        }
        return false;
    }

    public boolean hasNumber(String password) {
        int count = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                count++;
            }
            if (count == PasswordConstants.NUMBER_COUNT) return true;
        }
        return false;
    }

    public boolean hasSpecialSign(String password) {
        for (char c : password.toCharArray()) {
            if (PasswordConstants.ALLOWED_SPECIAL.indexOf(c) >= 0) return true;
        }
        return false;
    }

    public boolean containsOnlyAllowedSpecialSigns(String password) {
        return PasswordConstants.PASSWORD_CHARS.matcher(password).matches();
    }
}
