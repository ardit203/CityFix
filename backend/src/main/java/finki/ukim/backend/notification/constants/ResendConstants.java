package finki.ukim.backend.notification.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResendConstants {
    public static String API_KEY;
    public static String FROM_EMAIL;

    @Value("${resend.api-key}")
    public void setSecretKey(String apiKey) {
        API_KEY = apiKey;
    }

    @Value("${resend.from-email}")
    public void setTokenPrefix(String fromEmail) {
        FROM_EMAIL = fromEmail;
    }
}