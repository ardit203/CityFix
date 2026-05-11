package finki.ukim.backend.notification.listener;

import finki.ukim.backend.notification.model.enums.NotificationType;
import finki.ukim.backend.notification.model.events.*;
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

    @Async
    @EventListener
    public void handleRequestCreatedEvent(RequestCreatedEvent event) {
        log.info("Received RequestCreatedEvent for Request ID: {}", event.requestId());

        String emailText = emailTemplateService.getRequestCreatedTemplate(
                event.citizen().getUsername(),
                event.requestId(),
                event.title()
        );

        notificationService.createAndSendNotification(
                event.citizen(),
                NotificationType.REQUEST_CREATED,
                "Request Submitted - CityFix",
                emailText
        );
    }

    @Async
    @EventListener
    public void handleRequestAssignedEvent(RequestAssignedEvent event) {
        log.info("Received RequestAssignedEvent for Request ID: {}", event.requestId());

        String emailText = emailTemplateService.getRequestAssignedTemplate(
                event.assignedEmployee().getUsername(),
                event.requestId(),
                event.title()
        );

        notificationService.createAndSendNotification(
                event.assignedEmployee(),
                NotificationType.REQUEST_ASSIGNED,
                "New Request Assigned To You - CityFix",
                emailText
        );
    }

    @Async
    @EventListener
    public void handleRequestStatusChangedEvent(RequestStatusChangedEvent event) {
        log.info("Received RequestStatusChangedEvent for Request ID: {}, new status: {}", event.requestId(), event.newStatus());

        String emailText = emailTemplateService.getRequestStatusChangedTemplate(
                event.citizen().getUsername(),
                event.requestId(),
                event.title(),
                event.newStatus()
        );

        notificationService.createAndSendNotification(
                event.citizen(),
                NotificationType.REQUEST_STATUS_CHANGED,
                "Your Request Status Has Changed - CityFix",
                emailText
        );
    }

    @Async
    @EventListener
    public void handleRequestCommentAddedEvent(RequestCommentAdded event) {
        log.info("Received RequestCommentAdded for Request ID: {}", event.requestId());

        String emailText = emailTemplateService.getRequestCommentAddedTemplate(
                event.citizen().getUsername(),
                event.requestId(),
                event.title(),
                event.commentAuthor()
        );

        notificationService.createAndSendNotification(
                event.citizen(),
                NotificationType.REQUEST_COMMENT_ADDED,
                "New Comment on Your Request - CityFix",
                emailText
        );
    }
}
