package finki.ukim.backend.notification.service.domain;

public interface EmailTemplateService {
    String getPasswordResetTemplate(String username, String resetToken);
    String getRequestCreatedTemplate(String username, String ticketId);
    String getRequestStatusChangedTemplate(String username, String ticketId, String newStatus);
}
