package finki.ukim.backend.auth_and_access.helper;

import finki.ukim.backend.auth_and_access.model.exception.*;

import java.util.regex.Pattern;

public class PasswordChecker {
    public static int minPasswordLength = 8;
    public static int maxPasswordLength = 36;
    public static int upperCaseCount = 2;
    public static int numberCount = 2;
    public static String ALLOWED_SPECIAL = ".!@#$%^&";// "!@#$%^&*()-_=+[]{};:'\",.<>?/\\|`~";
    public static String message = String.format("Your password must be at least %d and at most %d characters long, " +
            "include at least %d uppercase letter (A–Z) and at least %d number (0–9), " +
            "and contain at least one special character from %s. " +
            "Only Latin letters (A–Z, a–z), numbers (0–9), and these special characters are allowed: %s. No other symbols or emojis are permitted.", minPasswordLength, maxPasswordLength, upperCaseCount, numberCount, ALLOWED_SPECIAL, ALLOWED_SPECIAL);
    private static final Pattern PASSWORD_CHARS = Pattern.compile("^[A-Za-z0-9" + Pattern.quote(ALLOWED_SPECIAL) + "]+$");



    public static void checkPassword(String password) {
        if (password.length() < minPasswordLength)
            throw new PasswordLengthException(minPasswordLength);

        if(password.length() > maxPasswordLength){
            throw new PasswordLengthExceededException(maxPasswordLength);
        }

        if (!containsOnlyAllowedSpecialSigns(password))
            throw new PasswordSpecialSignNotAllowedException(ALLOWED_SPECIAL);

        if (!hasUpper(password))
            throw new PasswordUpperCaseException(upperCaseCount);

        if (!hasNumber(password))
            throw new PasswordNumberException(numberCount);

        if (!hasSpecialSign(password))
            throw new PasswordSpecialSignException(ALLOWED_SPECIAL);
    }


    public static boolean hasUpper(String password) {
        int count = 0;
        char [] passwordChars = password.toCharArray();
        int size = passwordChars.length;
        for (int i=0 ; i<size ; i++) {
            boolean isUpper = Character.isUpperCase(passwordChars[i]);
            if (isUpper) {
                count++;
            }
            if(count == upperCaseCount) return true;
        }
        return false;
    }

    public static boolean hasNumber(String password) {
        int count = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)){
                count++;
            }
            if(count == numberCount) return true;
        }
        return false;
    }

    public static boolean hasSpecialSign(String password) {
        for (char c : password.toCharArray()) {
            if (ALLOWED_SPECIAL.indexOf(c) >= 0) return true;
        }
        return false;
    }

    public static boolean containsOnlyAllowedSpecialSigns(String password) {
        return PASSWORD_CHARS.matcher(password).matches();
    }
}
