package finki.ukim.backend.request_management.service.application.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.AiSuggestionResponseDto;
import finki.ukim.backend.request_management.model.dto.ProcessAiSuggestionDto;
import finki.ukim.backend.request_management.model.dto.RejectAiSuggestionDto;
import finki.ukim.backend.request_management.service.application.RequestAiSuggestionApplicationService;
import finki.ukim.backend.request_management.service.domain.RequestAiSuggestionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RequestAiSuggestionApplicationServiceImpl implements RequestAiSuggestionApplicationService {

    private final RequestAiSuggestionService requestAiSuggestionService;

    @Override
    public AiSuggestionResponseDto findSuggestion(Long requestId, User currentUser) {
        return AiSuggestionResponseDto.from(
                requestAiSuggestionService.findSuggestion(requestId, currentUser)
        );
    }

    @Override
    public void processSuggestion(Long requestId, User currentUser, ProcessAiSuggestionDto dto) {
        requestAiSuggestionService.processSuggestion(requestId, currentUser, dto);
    }

    @Override
    public void rejectSuggestion(Long requestId, User currentUser, RejectAiSuggestionDto dto) {
        requestAiSuggestionService.rejectSuggestion(requestId, currentUser, dto);
    }
}
