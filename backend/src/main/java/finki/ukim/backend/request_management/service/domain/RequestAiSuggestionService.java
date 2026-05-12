package finki.ukim.backend.request_management.service.domain;

import finki.ukim.backend.ai_integration.model.domain.AiSuggestion;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.AiSuggestionResponseDto;
import finki.ukim.backend.request_management.model.dto.ProcessAiSuggestionDto;
import finki.ukim.backend.request_management.model.dto.RejectAiSuggestionDto;

public interface RequestAiSuggestionService {
    AiSuggestion findSuggestion(Long requestId, User user);
    void processSuggestion(Long requestId, User user, ProcessAiSuggestionDto dto);
    void rejectSuggestion(Long requestId, User user, RejectAiSuggestionDto dto);
}
