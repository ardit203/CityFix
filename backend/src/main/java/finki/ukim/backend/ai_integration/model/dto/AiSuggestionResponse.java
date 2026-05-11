package finki.ukim.backend.ai_integration.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiSuggestionResponse {
    private Long suggestionId;
    private String suggestedCategoryName;
    private String suggestedPriority;
    private String aiSummary;
    private boolean successful;
    private String message;

    public static AiSuggestionResponse success(Long suggestionId, String suggestedCategoryName, String suggestedPriority, String aiSummary) {
        return new AiSuggestionResponse(suggestionId, suggestedCategoryName, suggestedPriority, aiSummary, true, "Success");
    }

    public static AiSuggestionResponse failure(String message) {
        return new AiSuggestionResponse(null, null, null, null, false, message);
    }
}
