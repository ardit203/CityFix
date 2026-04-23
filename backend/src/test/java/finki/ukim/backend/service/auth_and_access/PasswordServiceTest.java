package finki.ukim.backend.service.auth_and_access;

import finki.ukim.backend.auth_and_access.helper.PasswordHelper;
import finki.ukim.backend.auth_and_access.model.exception.InvalidCurrentPasswordException;
import finki.ukim.backend.auth_and_access.model.exception.NewPasswordCannotBeSameAsOldException;
import finki.ukim.backend.auth_and_access.model.exception.PasswordCannotBeEmptyException;
import finki.ukim.backend.auth_and_access.model.exception.PasswordLengthExceededException;
import finki.ukim.backend.auth_and_access.model.exception.PasswordLengthException;
import finki.ukim.backend.auth_and_access.model.exception.PasswordNumberException;
import finki.ukim.backend.auth_and_access.model.exception.PasswordSpecialSignException;
import finki.ukim.backend.auth_and_access.model.exception.PasswordSpecialSignNotAllowedException;
import finki.ukim.backend.auth_and_access.model.exception.PasswordUpperCaseException;
import finki.ukim.backend.auth_and_access.model.exception.PasswordsDoNotMatchException;
import finki.ukim.backend.auth_and_access.service.domain.impl.PasswordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PasswordServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private PasswordHelper passwordHelper;

    @InjectMocks
    private PasswordServiceImpl passwordService;

    // A valid password satisfying all rules: >= 8 chars, 2 uppercase, 2 numbers, 1 allowed special
    private static final String VALID_PASSWORD    = "TestPass11!";
    private static final String ENCODED_PASSWORD  = "$2a$10$encodedHash";

    @BeforeEach
    void setUp() {
    }

    // ── preparePasswordForRegistration ────────────────────────────────────

    @Test
    void prepareForRegistration_shouldReturnEncodedPassword_whenValid() {
        when(passwordEncoder.encode(VALID_PASSWORD)).thenReturn(ENCODED_PASSWORD);

        String result = passwordService.preparePasswordForRegistration(VALID_PASSWORD, VALID_PASSWORD);

        assertThat(result).isEqualTo(ENCODED_PASSWORD);
    }

    @Test
    void prepareForRegistration_shouldThrow_whenPasswordIsNull() {
        assertThatThrownBy(() ->
                passwordService.preparePasswordForRegistration(null, VALID_PASSWORD)
        ).isInstanceOf(PasswordCannotBeEmptyException.class);
    }

    @Test
    void prepareForRegistration_shouldThrow_whenConfirmPasswordIsNull() {
        assertThatThrownBy(() ->
                passwordService.preparePasswordForRegistration(VALID_PASSWORD, null)
        ).isInstanceOf(PasswordCannotBeEmptyException.class);
    }

    @Test
    void prepareForRegistration_shouldThrow_whenPasswordsDoNotMatch() {
        assertThatThrownBy(() ->
                passwordService.preparePasswordForRegistration(VALID_PASSWORD, "Different1!")
        ).isInstanceOf(PasswordsDoNotMatchException.class);
    }

    @Test
    void prepareForRegistration_shouldThrow_whenPasswordTooShort() {
        String tooShort = "Ab1!";  // < 8 chars
        assertThatThrownBy(() ->
                passwordService.preparePasswordForRegistration(tooShort, tooShort)
        ).isInstanceOf(PasswordLengthException.class);
    }

    @Test
    void prepareForRegistration_shouldThrow_whenPasswordExceedsMaxLength() {
        String tooLong = "AA11!".repeat(8); // > 36 chars
        assertThatThrownBy(() ->
                passwordService.preparePasswordForRegistration(tooLong, tooLong)
        ).isInstanceOf(PasswordLengthExceededException.class);
    }

    @Test
    void prepareForRegistration_shouldThrow_whenPasswordMissingUppercase() {
        String noUpper = "testpass11!"; // no uppercase
        assertThatThrownBy(() ->
                passwordService.preparePasswordForRegistration(noUpper, noUpper)
        ).isInstanceOf(PasswordUpperCaseException.class);
    }

    @Test
    void prepareForRegistration_shouldThrow_whenPasswordMissingNumber() {
        String noNumber = "TestPass!!"; // no digits
        assertThatThrownBy(() ->
                passwordService.preparePasswordForRegistration(noNumber, noNumber)
        ).isInstanceOf(PasswordNumberException.class);
    }

    @Test
    void prepareForRegistration_shouldThrow_whenPasswordMissingSpecialChar() {
        String noSpecial = "TestPass11"; // no special char
        assertThatThrownBy(() ->
                passwordService.preparePasswordForRegistration(noSpecial, noSpecial)
        ).isInstanceOf(PasswordSpecialSignException.class);
    }

    @Test
    void prepareForRegistration_shouldThrow_whenPasswordContainsDisallowedChar() {
        String disallowed = "TestPass11~"; // ~ is not in ALLOWED_SPECIAL
        assertThatThrownBy(() ->
                passwordService.preparePasswordForRegistration(disallowed, disallowed)
        ).isInstanceOf(PasswordSpecialSignNotAllowedException.class);
    }

    // ── preparePasswordForChange ──────────────────────────────────────────

    @Test
    void prepareForChange_shouldReturnEncodedPassword_whenValid() {
        String newPassword = "NewPass11!";
        when(passwordEncoder.matches(VALID_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);  // current matches
        when(passwordEncoder.matches(newPassword, ENCODED_PASSWORD)).thenReturn(false);    // new is different
        when(passwordEncoder.encode(newPassword)).thenReturn("$2a$10$newHash");

        String result = passwordService.preparePasswordForChange(
                VALID_PASSWORD, ENCODED_PASSWORD, newPassword, newPassword);

        assertThat(result).isEqualTo("$2a$10$newHash");
    }

    @Test
    void prepareForChange_shouldThrow_whenCurrentPasswordIsWrong() {
        when(passwordEncoder.matches("wrongPw", ENCODED_PASSWORD)).thenReturn(false);

        assertThatThrownBy(() ->
                passwordService.preparePasswordForChange("wrongPw", ENCODED_PASSWORD, VALID_PASSWORD, VALID_PASSWORD)
        ).isInstanceOf(InvalidCurrentPasswordException.class);
    }

    @Test
    void prepareForChange_shouldThrow_whenNewPasswordSameAsOld() {
        when(passwordEncoder.matches(VALID_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(passwordEncoder.matches(VALID_PASSWORD, ENCODED_PASSWORD)).thenReturn(true); // new == old

        assertThatThrownBy(() ->
                passwordService.preparePasswordForChange(VALID_PASSWORD, ENCODED_PASSWORD, VALID_PASSWORD, VALID_PASSWORD)
        ).isInstanceOf(NewPasswordCannotBeSameAsOldException.class);
    }

    @Test
    void prepareForChange_shouldThrow_whenAnyRequiredFieldIsBlank() {
        assertThatThrownBy(() ->
                passwordService.preparePasswordForChange("", ENCODED_PASSWORD, VALID_PASSWORD, VALID_PASSWORD)
        ).isInstanceOf(PasswordCannotBeEmptyException.class);
    }

    // ── validatePasswordPolicy ────────────────────────────────────────────

    @Test
    void matches_shouldDelegateToPasswordEncoder() {
        when(passwordEncoder.matches("raw", ENCODED_PASSWORD)).thenReturn(true);

        assertThat(passwordService.matches("raw", ENCODED_PASSWORD)).isTrue();
    }
}
