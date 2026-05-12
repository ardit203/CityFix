package finki.ukim.backend.request_management.service.application;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.AiSuggestionResponseDto;
import finki.ukim.backend.request_management.model.dto.ProcessAiSuggestionDto;
import finki.ukim.backend.request_management.model.dto.RejectAiSuggestionDto;

public interface RequestAiSuggestionAppService {
    AiSuggestionResponseDto findSuggestion(Long requestId, User currentUser);
    void processSuggestion(Long requestId, User currentUser, ProcessAiSuggestionDto dto);
    void rejectSuggestion(Long requestId, User currentUser, RejectAiSuggestionDto dto);
}
