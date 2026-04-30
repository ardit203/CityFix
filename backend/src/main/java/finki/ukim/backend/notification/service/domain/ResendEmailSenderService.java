package finki.ukim.backend.notification.service.domain;

public interface ResendEmailSenderService {
    String sendEmail(String to, String subject, String textContent);
}
