package finki.ukim.backend.auth_and_access.service.domain.impl;

import finki.ukim.backend.auth_and_access.helper.PasswordResetHelper;
import finki.ukim.backend.auth_and_access.model.domain.PasswordResetToken;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.exception.*;
import finki.ukim.backend.auth_and_access.repository.PasswordResetTokenRepository;
import finki.ukim.backend.auth_and_access.service.domain.PasswordResetService;
import finki.ukim.backend.auth_and_access.service.domain.PasswordService;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import finki.ukim.backend.notification.model.events.PasswordResetEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userService.findByEmail(email);

        passwordResetTokenRepository.invalidateAllActiveByUserId(user.getId());

        String rawToken = passwordResetHelper.generateToken();
        String tokenHash = passwordResetHelper.hashToken(rawToken);

        PasswordResetToken passwordResetToken = new PasswordResetToken(user, tokenHash, RESET_TOKEN_EXPIRATION_MINUTES);

        passwordResetTokenRepository.save(passwordResetToken);

        eventPublisher.publishEvent(new PasswordResetEvent(user, rawToken));
    }

    @Override
    @Transactional
    public User resetPassword(String token, String newPassword, String confirmPassword) {
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

        return userService.save(user);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByTokenHash(
                        passwordResetHelper.hashToken(token)
                )
                .orElseThrow(InvalidTokenException::new);
    }

    @Override
    public PasswordResetToken findActiveByUserId(Long userId) {
        return passwordResetTokenRepository
                .findActiveByUserId(userId)
                .orElseThrow(() -> new NoActiveTokenException(userId));
    }

    @Override
    @Transactional
    public void deleteInactiveTokens() {
        passwordResetTokenRepository.deleteAll(
                passwordResetTokenRepository.findAllInactiveTokens()
        );
    }
}
