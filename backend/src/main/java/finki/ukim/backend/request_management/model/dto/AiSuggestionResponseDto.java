package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.ai_integration.model.domain.AiSuggestion;
import finki.ukim.backend.ai_integration.model.dto.AiSuggestionResponse;
import finki.ukim.backend.ai_integration.model.enums.SuggestionStatus;
import finki.ukim.backend.request_management.model.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record AiSuggestionResponseDto(
        Long id,
        Long requestId,
        Long categoryId,
//        String categoryName,
        Priority priority,
        String aiSummary,
        SuggestionStatus status
) {
    public static AiSuggestionResponseDto from(AiSuggestion aiSuggestion) {
        return new AiSuggestionResponseDto(
                aiSuggestion.getId(),
                aiSuggestion.getRequest().getId(),
                aiSuggestion.getCategory() == null ? null : aiSuggestion.getCategory().getId(),
                aiSuggestion.getPriority(),
                aiSuggestion.getAiSummary(),
                aiSuggestion.getSuggestionStatus()
        );
    }
}
