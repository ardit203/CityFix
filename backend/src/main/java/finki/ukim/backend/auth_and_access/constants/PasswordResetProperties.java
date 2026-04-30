package finki.ukim.backend.auth_and_access.constants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth.password-reset")
@Getter
@Setter
public class PasswordResetProperties {
    private String baseUrl;
}