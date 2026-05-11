package finki.ukim.backend.ai_integration.service.infrastructure;

import finki.ukim.backend.ai_integration.config.OpenAiProperties;
import finki.ukim.backend.ai_integration.model.dto.AiFileContext;
import finki.ukim.backend.ai_integration.model.dto.openai.OpenAiRequest;
import finki.ukim.backend.ai_integration.model.dto.openai.OpenAiResponse;
import finki.ukim.backend.ai_integration.service.port.LlmClientPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

//@Component
public class OpenAiClientAdapter implements LlmClientPort {

    private final OpenAiProperties properties;
    private final RestTemplate restTemplate;
    
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public OpenAiClientAdapter(OpenAiProperties properties) {
        this.properties = properties;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String getCompletion(String systemPrompt, String userMessage, List<AiFileContext> files) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getApiKey());

        OpenAiRequest requestBody = new OpenAiRequest(
                properties.getModel(),
                new OpenAiRequest.ResponseFormat("json_object"),
                List.of(
                        new OpenAiRequest.Message("system", systemPrompt),
                        new OpenAiRequest.Message("user", userMessage)
                )
        );

        HttpEntity<OpenAiRequest> request = new HttpEntity<>(requestBody, headers);

        try {
            OpenAiResponse response = restTemplate.postForObject(OPENAI_API_URL, request, OpenAiResponse.class);
            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to communicate with OpenAI API", e);
        }

        throw new RuntimeException("Empty response from OpenAI API");
    }
}
