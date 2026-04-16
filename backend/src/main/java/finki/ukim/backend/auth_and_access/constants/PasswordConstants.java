package finki.ukim.backend.auth_and_access.constants;

import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
public class PasswordConstants {
    public static int MIN_PASSWORD_LENGTH = 8;
    public static int MAX_PASSWORD_LENGTH = 36;
    public static int UPPERCASE_COUNT = 2;
    public static int NUMBER_COUNT = 2;
    public static String ALLOWED_SPECIAL = ".!@#$%^&";// "!@#$%^&*()-_=+[]{};:'\",.<>?/\\|`~";
    public static Pattern PASSWORD_CHARS = Pattern.compile("^[A-Za-z0-9" + Pattern.quote(ALLOWED_SPECIAL) + "]+$");
    public static String MESSAGE = String.format("Your password must be at least %d and at most %d characters long, " +
            "include at least %d uppercase letter (A–Z) and at least %d number (0–9), " +
            "and contain at least one special character from %s. " +
            "Only Latin letters (A–Z, a–z), numbers (0–9), and these special characters are allowed: %s. No other symbols or emojis are permitted.", MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH, UPPERCASE_COUNT, NUMBER_COUNT, ALLOWED_SPECIAL, ALLOWED_SPECIAL);

    public static void setPasswordChars(String allowedSpecial) {
        PASSWORD_CHARS = Pattern.compile("^[A-Za-z0-9" + Pattern.quote(allowedSpecial) + "]+$");;
    }

    public static void setMinPasswordLength(int minPasswordLength) {
        MIN_PASSWORD_LENGTH = minPasswordLength;
    }

    public static void setMaxPasswordLength(int maxPasswordLength) {
        MAX_PASSWORD_LENGTH = maxPasswordLength;
    }

    public static void setUppercaseCount(int uppercaseCount) {
        UPPERCASE_COUNT = uppercaseCount;
    }

    public static void setNumberCount(int numberCount) {
        NUMBER_COUNT = numberCount;
    }

    public static void setAllowedSpecial(String allowedSpecial) {
        ALLOWED_SPECIAL = allowedSpecial;
        setPasswordChars(allowedSpecial);
    }

    public static void setMESSAGE(String MESSAGE) {
        PasswordConstants.MESSAGE = MESSAGE;
    }
}
