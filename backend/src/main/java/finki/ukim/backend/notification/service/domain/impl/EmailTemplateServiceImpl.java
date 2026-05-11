package finki.ukim.backend.notification.service.domain.impl;

import finki.ukim.backend.notification.service.domain.EmailTemplateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    public String getPasswordResetTemplate(String username, String resetToken) {
        return """
                Hello {{username}},
                
                You recently requested to reset your password for your CityFix account.
                Please use the following token to complete the process:
                
                Reset link: {{resetLink}}
                
                If you did not request a password reset, please ignore this email.
                
                Best regards,
                The CityFix Team
                """
                .replace("{{username}}", username != null ? username : "User")
                .replace("{{resetLink}}", frontendUrl + "/reset-password?token=" + resetToken);
    }

    @Override
    public String getRequestCreatedTemplate(String username, Long requestId, String title) {
        return """
                Hello {{username}},
                
                Your request has been successfully submitted to CityFix!
                
                Request ID: #{{requestId}}
                Title: {{title}}
                
                Our team will review your issue and get back to you shortly.
                You can track the status of your request by logging into your CityFix account.
                
                Thank you for helping improve your city!
                
                Best regards,
                The CityFix Team
                """
                .replace("{{username}}", username != null ? username : "User")
                .replace("{{requestId}}", String.valueOf(requestId))
                .replace("{{title}}", title != null ? title : "N/A");
    }

    @Override
    public String getRequestStatusChangedTemplate(String username, Long requestId, String title, String newStatus) {
        return """
                Hello {{username}},
                
                There's an update on your CityFix request.
                
                Request ID: #{{requestId}}
                Title: {{title}}
                New Status: {{newStatus}}
                
                Log in to your CityFix account to view the full details.
                
                Best regards,
                The CityFix Team
                """
                .replace("{{username}}", username != null ? username : "User")
                .replace("{{requestId}}", String.valueOf(requestId))
                .replace("{{title}}", title != null ? title : "N/A")
                .replace("{{newStatus}}", newStatus != null ? newStatus : "Unknown");
    }

    @Override
    public String getRequestAssignedTemplate(String username, Long requestId, String title) {
        return """
                Hello {{username}},
                
                A new request has been assigned to you in CityFix.
                
                Request ID: #{{requestId}}
                Title: {{title}}
                
                Please log in to your CityFix account to review and handle this request.
                
                Best regards,
                The CityFix Team
                """
                .replace("{{username}}", username != null ? username : "Employee")
                .replace("{{requestId}}", String.valueOf(requestId))
                .replace("{{title}}", title != null ? title : "N/A");
    }

    @Override
    public String getRequestCommentAddedTemplate(String username, Long requestId, String title, String commentAuthor) {
        return """
                Hello {{username}},
                
                A new comment has been added to your CityFix request.
                
                Request ID: #{{requestId}}
                Title: {{title}}
                Comment by: {{commentAuthor}}
                
                Log in to your CityFix account to read the full comment and respond if needed.
                
                Best regards,
                The CityFix Team
                """
                .replace("{{username}}", username != null ? username : "User")
                .replace("{{requestId}}", String.valueOf(requestId))
                .replace("{{title}}", title != null ? title : "N/A")
                .replace("{{commentAuthor}}", commentAuthor != null ? commentAuthor : "Unknown");
    }
}
