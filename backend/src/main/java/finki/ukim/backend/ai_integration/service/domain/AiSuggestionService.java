package finki.ukim.backend.ai_integration.service.domain;

import finki.ukim.backend.ai_integration.model.dto.AiSuggestionRequest;
import finki.ukim.backend.request_management.model.domain.Request;

public interface AiSuggestionService {
    public void suggest(Request request);

}
