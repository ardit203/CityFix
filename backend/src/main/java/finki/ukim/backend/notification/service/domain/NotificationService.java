package finki.ukim.backend.notification.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.notification.model.domain.Notification;
import finki.ukim.backend.notification.model.enums.NotificationType;

public interface NotificationService {
    Notification createAndSendNotification(User recipient, NotificationType type, String subject, String messageText);
}
