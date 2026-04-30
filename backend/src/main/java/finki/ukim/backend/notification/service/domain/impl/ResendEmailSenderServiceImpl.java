package finki.ukim.backend.notification.service.domain.impl;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import finki.ukim.backend.notification.constants.ResendConstants;
import finki.ukim.backend.notification.service.domain.ResendEmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResendEmailSenderServiceImpl implements ResendEmailSenderService {

    @Override
    public String sendEmail(String to, String subject, String textContent) {
        if (ResendConstants.API_KEY == null || ResendConstants.API_KEY.isBlank() || ResendConstants.API_KEY.contains("RESEND_API_KEY")) {
            log.warn("Resend API key is not properly configured. Skipping email sending to: {}", to);
            return "simulated-id-" + System.currentTimeMillis();
        }

        Resend resend = new Resend(ResendConstants.API_KEY);

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from(ResendConstants.FROM_EMAIL)
                .to(to)
                .subject(subject)
                .text(textContent)
                .build();

        try {
            SendEmailResponse data = resend.emails().send(sendEmailRequest);
            return data.getId();
        } catch (ResendException e) {
            log.error("Failed to send email to {} via Resend", to, e);
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
