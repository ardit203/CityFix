package finki.ukim.backend.ai_integration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai.gemini")
public class GeminiProperties {
    private String apiKey;
    private String model = "gemini-1.5-flash";
}
