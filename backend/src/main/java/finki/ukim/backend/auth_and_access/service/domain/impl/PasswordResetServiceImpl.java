package finki.ukim.backend.auth_and_access.service.domain.impl;

import finki.ukim.backend.auth_and_access.helper.PasswordResetHelper;
import finki.ukim.backend.auth_and_access.model.domain.PasswordResetToken;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.exception.InvalidTokenException;
import finki.ukim.backend.auth_and_access.model.exception.TokenNotFoundException;
import finki.ukim.backend.auth_and_access.model.exception.UserWithEmailDoesNotExistException;
import finki.ukim.backend.auth_and_access.repository.PasswordResetTokenRepository;
import finki.ukim.backend.auth_and_access.service.domain.PasswordResetService;
import finki.ukim.backend.auth_and_access.service.domain.PasswordService;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private static final int RESET_TOKEN_EXPIRATION_MINUTES = 5;

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordResetHelper passwordResetHelper;
    private final UserService userService;
    private final PasswordService passwordService;
    // private final EmailService emailService; // add your mail service here

    @Override
    @Transactional
    public String requestPasswordReset(String email) {
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return "Email address not found. Please check and try again.";
        }

        User user = optionalUser.get();

        passwordResetTokenRepository.invalidateAllActiveByUserId(user.getId());

        String rawToken = passwordResetHelper.generateToken();
        String tokenHash = passwordResetHelper.hashToken(rawToken);

        PasswordResetToken passwordResetToken = new PasswordResetToken(user, tokenHash, RESET_TOKEN_EXPIRATION_MINUTES);

        passwordResetTokenRepository.save(passwordResetToken);

        // Send the raw token in email, never the hash
        // emailService.sendPasswordResetEmail(user.getEmail(), rawToken);
        return rawToken;
    }

    @Override
    @Transactional
    public Optional<User> resetPassword(String token, String newPassword, String confirmPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository
                .findByTokenHashWithUser(passwordResetHelper.hashToken(token))
                .orElseThrow(TokenNotFoundException::new);

        if (!passwordResetToken.isActive()) {
            throw new InvalidTokenException();
        }

        String encodedNewPassword = passwordService.preparePasswordForRegistration(
                newPassword,
                confirmPassword
        );

        User user = passwordResetToken.getUser();
        user.setPassword(encodedNewPassword);

        passwordResetToken.markAsUsed();

        passwordResetTokenRepository.invalidateOtherActiveByUserId(
                user.getId(),
                passwordResetToken.getId()
        );

        return Optional.of(userService.save(user));
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordResetTokenRepository.findByTokenHash(
                passwordResetHelper.hashToken(token)
        );
    }

    @Override
    public Optional<PasswordResetToken> findActiveByUserId(Long userId) {
        return passwordResetTokenRepository.findActiveByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteInactiveTokens() {
        passwordResetTokenRepository.deleteAll(
                passwordResetTokenRepository.findAllInactiveTokens()
        );
    }
}
