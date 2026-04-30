package finki.ukim.backend.notification.listener;

import finki.ukim.backend.notification.model.enums.NotificationType;
import finki.ukim.backend.notification.model.events.PasswordResetEvent;
import finki.ukim.backend.notification.service.domain.EmailTemplateService;
import finki.ukim.backend.notification.service.domain.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final EmailTemplateService emailTemplateService;

    @Async
    @EventListener
    public void handlePasswordResetEvent(PasswordResetEvent event) {
        log.info("Received PasswordResetEvent for user: {}", event.user().getUsername());
        
        String emailText = emailTemplateService.getPasswordResetTemplate(
                event.user().getUsername(), 
                event.resetToken()
        );
        
        notificationService.createAndSendNotification(
                event.user(),
                NotificationType.PASSWORD_RESET,
                "Password Reset Request - CityFix",
                emailText
        );
    }
}
