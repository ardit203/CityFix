package finki.ukim.backend.notification.service.domain.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.notification.model.domain.Notification;
import finki.ukim.backend.notification.model.enums.NotificationStatus;
import finki.ukim.backend.notification.model.enums.NotificationType;
import finki.ukim.backend.notification.repository.NotificationRepository;
import finki.ukim.backend.notification.service.domain.NotificationService;
import finki.ukim.backend.notification.service.domain.ResendEmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ResendEmailSenderService emailSenderService;

    @Override
    public Notification createAndSendNotification(User recipient, NotificationType type, String subject, String messageText) {
        Notification notification = new Notification(recipient, type, subject, messageText);
        notification = notificationRepository.save(notification);

        try {
            String messageId = emailSenderService.sendEmail(recipient.getEmail(), subject, messageText);
            
            notification.setStatus(NotificationStatus.SENT);
            notification.setProviderMessageId(messageId);
            notification.setSentAt(LocalDateTime.now());
            log.info("Successfully sent {} notification to {}", type, recipient.getEmail());
        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setFailureReason(e.getMessage());
            notification.setRetryCount(notification.getRetryCount() + 1);
            log.error("Failed to send {} notification to {}", type, recipient.getEmail(), e);
        }

        notification.setLastTriedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }
}
