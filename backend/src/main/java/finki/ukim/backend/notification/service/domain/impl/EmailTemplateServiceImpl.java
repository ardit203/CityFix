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
        String template = """
                Hello {{username}},
                
                You recently requested to reset your password for your CityFix account.
                Please use the following token to complete the process:
                
                Token: {{resetToken}}
                
                If you did not request a password reset, please ignore this email.
                
                Best regards,
                The CityFix Team
                """;
        
        return template.replace("{{username}}", username != null ? username : "User")
                       .replace("{{resetToken}}", resetToken);
    }

    @Override
    public String getRequestCreatedTemplate(String username, String ticketId) {
        String template = """
                Hello {{username}},
                
                Your request #{{ticketId}} has been successfully created.
                Our team will review it shortly.
                
                Best regards,
                The CityFix Team
                """;
        
        return template.replace("{{username}}", username != null ? username : "User")
                       .replace("{{ticketId}}", ticketId);
    }

    @Override
    public String getRequestStatusChangedTemplate(String username, String ticketId, String newStatus) {
        String template = """
                Hello {{username}},
                
                The status of your request #{{ticketId}} has been updated to: {{newStatus}}.
                
                Best regards,
                The CityFix Team
                """;
        
        return template.replace("{{username}}", username != null ? username : "User")
                       .replace("{{ticketId}}", ticketId)
                       .replace("{{newStatus}}", newStatus);
    }
}
