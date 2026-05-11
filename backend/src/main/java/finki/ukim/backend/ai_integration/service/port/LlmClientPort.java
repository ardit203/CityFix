package finki.ukim.backend.ai_integration.service.port;

import finki.ukim.backend.ai_integration.model.dto.AiFileContext;

import java.util.List;

public interface LlmClientPort {
    /**
     * Sends a prompt to the LLM and returns the completion response.
     *
     * @param systemPrompt The instructions describing how the LLM should behave.
     * @param userMessage  The actual user input to be processed.
     * @param files        Optional list of image files (Base64 encoded) to provide visual context.
     * @return The raw text/JSON completion from the LLM.
     */
    String getCompletion(String systemPrompt, String userMessage, List<AiFileContext> files);
}
