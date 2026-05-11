package finki.ukim.backend.notification.service.domain;

public interface EmailTemplateService {
    String getPasswordResetTemplate(String username, String resetToken);
    String getRequestCreatedTemplate(String username, Long requestId, String title);
    String getRequestStatusChangedTemplate(String username, Long requestId, String title, String newStatus);
    String getRequestAssignedTemplate(String username, Long requestId, String title);
    String getRequestCommentAddedTemplate(String username, Long requestId, String title, String commentAuthor);
}
