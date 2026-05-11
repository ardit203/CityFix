package finki.ukim.backend.ai_integration.service.infrastructure;

import finki.ukim.backend.ai_integration.config.GeminiProperties;
import finki.ukim.backend.ai_integration.model.dto.AiFileContext;
import finki.ukim.backend.ai_integration.model.dto.gemini.GeminiRequest;
import finki.ukim.backend.ai_integration.model.dto.gemini.GeminiResponse;
import finki.ukim.backend.ai_integration.service.port.LlmClientPort;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class GeminiClientAdapter implements LlmClientPort {

    private final GeminiProperties properties;
    private final RestTemplate restTemplate;

    public GeminiClientAdapter(GeminiProperties properties) {
        this.properties = properties;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String getCompletion(String systemPrompt, String userMessage, List<AiFileContext> files) {
        String apiUrl = String.format(
                "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                properties.getModel(), properties.getApiKey()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build the parts list - always start with the combined text prompt
        List<GeminiRequest.Part> parts = new ArrayList<>();
        String combinedPrompt = "System Instruction: " + systemPrompt + "\n\nUser Input: " + userMessage;
        parts.add(new GeminiRequest.Part(combinedPrompt));

        // Append image parts if any files were provided
        if (files != null && !files.isEmpty()) {
            for (AiFileContext file : files) {
                GeminiRequest.InlineData inlineData = new GeminiRequest.InlineData(
                        file.getMimeType(),
                        file.getBase64Data()
                );
                parts.add(new GeminiRequest.Part(inlineData));
            }
        }

        GeminiRequest requestBody = new GeminiRequest(
                List.of(new GeminiRequest.Content("user", parts)),
                new GeminiRequest.GenerationConfig("application/json")
        );

        HttpEntity<GeminiRequest> request = new HttpEntity<>(requestBody, headers);

        try {
            GeminiResponse response = restTemplate.postForObject(apiUrl, request, GeminiResponse.class);

            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                GeminiResponse.Candidate candidate = response.getCandidates().get(0);
                if (candidate.getContent() != null
                        && candidate.getContent().getParts() != null
                        && !candidate.getContent().getParts().isEmpty()) {
                    return candidate.getContent().getParts().get(0).getText();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to communicate with Gemini API", e);
        }

        throw new RuntimeException("Empty response from Gemini API");
    }
}
