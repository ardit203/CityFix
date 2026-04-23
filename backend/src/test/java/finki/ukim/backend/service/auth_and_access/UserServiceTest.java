package finki.ukim.backend.service.auth_and_access;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.exception.*;
import finki.ukim.backend.auth_and_access.repository.UserRepository;
import finki.ukim.backend.auth_and_access.service.domain.PasswordService;
import finki.ukim.backend.auth_and_access.service.domain.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private UserServiceImpl userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser  = new User("alice", "$2a$10$encoded", "alice@example.com", Role.ROLE_CITIZEN);
    }

    // ── register ──────────────────────────────────────────────────────────

    @Test
    void register_shouldSaveAndReturnUser_whenValid() {
        User newUser = new User("bob", "RawPass11!", "bob@example.com", Role.ROLE_CITIZEN);

        when(userRepository.existsByUsername("bob")).thenReturn(false);
        when(userRepository.existsByEmail("bob@example.com")).thenReturn(false);
        when(passwordService.preparePasswordForRegistration("RawPass11!", "RawPass11!")).thenReturn("$enc");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.register(newUser, "RawPass11!");

        assertThat(result.getUsername()).isEqualTo("bob");
        assertThat(result.getPassword()).isEqualTo("$enc");
        verify(userRepository).save(newUser);
    }

    @Test
    void register_shouldThrow_whenUsernameAlreadyExists() {
        User newUser = new User("alice", "RawPass11!", "new@example.com", Role.ROLE_CITIZEN);

        when(userRepository.existsByUsername("alice")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(newUser, "RawPass11!"))
                .isInstanceOf(UsernameAlreadyExistsException.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrow_whenEmailAlreadyExists() {
        User newUser = new User("newuser", "RawPass11!", "alice@example.com", Role.ROLE_CITIZEN);

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(newUser, "RawPass11!"))
                .isInstanceOf(EmailAlreadyExistsException.class);
        verify(userRepository, never()).save(any());
    }

    // ── login ─────────────────────────────────────────────────────────────

    @Test
    void login_shouldReturnUser_whenCredentialsAreCorrect() {
        existingUser.setFailedLoginAttempts(0);
        existingUser.setLockedUntil(null);

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(existingUser));
        when(passwordService.matches("correct", "$2a$10$encoded")).thenReturn(true);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.login("alice", "correct");

        assertThat(result.getUsername()).isEqualTo("alice");
        assertThat(result.getFailedLoginAttempts()).isEqualTo(0);
    }

    @Test
    void login_shouldThrow_whenUserDoesNotExist() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login("ghost", "anything"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void login_shouldThrow_whenAccountIsLocked() {
        existingUser.setLockedUntil(LocalDateTime.now().plusHours(1));

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> userService.login("alice", "correct"))
                .isInstanceOf(AccountLockedException.class);
    }

    @Test
    void login_shouldThrow_whenPasswordIsWrong() {
        existingUser.setFailedLoginAttempts(0);
        existingUser.setLockedUntil(null);

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(existingUser));
        when(passwordService.matches("wrong", "$2a$10$encoded")).thenReturn(false);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        assertThatThrownBy(() -> userService.login("alice", "wrong"))
                .isInstanceOf(InvalidUserCredentialsException.class);
    }

    // ── handleFailedLogin ─────────────────────────────────────────────────

    @Test
    void handleFailedLogin_shouldIncrementAttempts_whenBelowMax() {
        existingUser.setFailedLoginAttempts(2);
        existingUser.setLockLevel(0);

        when(userRepository.save(existingUser)).thenReturn(existingUser);

        userService.handleFailedLogin(existingUser);

        assertThat(existingUser.getFailedLoginAttempts()).isEqualTo(3);
        assertThat(existingUser.getLockedUntil()).isNull();
    }

    @Test
    void handleFailedLogin_shouldLockAccount_whenAttemptCountReachesMax() {
        existingUser.setFailedLoginAttempts(4);  // next attempt is the 5th → lock
        existingUser.setLockLevel(0);

        when(userRepository.save(existingUser)).thenReturn(existingUser);

        userService.handleFailedLogin(existingUser);

        assertThat(existingUser.getFailedLoginAttempts()).isEqualTo(0);
        assertThat(existingUser.getLockLevel()).isEqualTo(1);
        assertThat(existingUser.getLockedUntil()).isNotNull();
        assertThat(existingUser.getLockedUntil()).isAfter(LocalDateTime.now());
    }

    // ── calculateLockedUntil ──────────────────────────────────────────────

    @Test
    void calculateLockedUntil_shouldReturnFiveMinutes_forLockLevel1() {
        LocalDateTime result = userService.calculateLockedUntil(1);
        assertThat(result).isBetween(
                LocalDateTime.now().plusMinutes(4),
                LocalDateTime.now().plusMinutes(6)
        );
    }

    @Test
    void calculateLockedUntil_shouldReturnThirtyMinutes_forLockLevel2() {
        LocalDateTime result = userService.calculateLockedUntil(2);
        assertThat(result).isBetween(
                LocalDateTime.now().plusMinutes(29),
                LocalDateTime.now().plusMinutes(31)
        );
    }

    @Test
    void calculateLockedUntil_shouldReturnTwoHours_forLockLevel3() {
        LocalDateTime result = userService.calculateLockedUntil(3);
        assertThat(result).isBetween(
                LocalDateTime.now().plusHours(1).plusMinutes(59),
                LocalDateTime.now().plusHours(2).plusMinutes(1)
        );
    }

    @Test
    void calculateLockedUntil_shouldReturn30Days_forUnknownLockLevel() {
        LocalDateTime result = userService.calculateLockedUntil(99);
        assertThat(result).isBetween(
                LocalDateTime.now().plusDays(29),
                LocalDateTime.now().plusDays(31)
        );
    }

    // ── changePassword ────────────────────────────────────────────────────

    @Test
    void changePassword_shouldUpdatePassword_whenValid() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(existingUser));
        when(passwordService.preparePasswordForChange(
                "currentRaw", "$2a$10$encoded", "NewPass11!", "NewPass11!")
        ).thenReturn("$newEncoded");
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        Optional<User> result = userService.changePassword("alice", "currentRaw", "NewPass11!", "NewPass11!");

        assertThat(result).isPresent();
        assertThat(result.get().getPassword()).isEqualTo("$newEncoded");
    }

    @Test
    void changePassword_shouldReturnEmpty_whenUserDoesNotExist() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        Optional<User> result = userService.changePassword("ghost", "old", "new", "new");

        assertThat(result).isEmpty();
    }

    // ── deleteById / deleteByUsername ─────────────────────────────────────

    @Test
    void deleteById_shouldReturnDeletedUser_whenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        Optional<User> result = userService.deleteById(1L);

        assertThat(result).isPresent();
        verify(userRepository).delete(existingUser);
    }

    @Test
    void deleteById_shouldReturnEmpty_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.deleteById(99L);

        assertThat(result).isEmpty();
        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteByUsername_shouldReturnDeletedUser_whenExists() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(existingUser));

        Optional<User> result = userService.deleteByUsername("alice");

        assertThat(result).isPresent();
        verify(userRepository).delete(existingUser);
    }
}
