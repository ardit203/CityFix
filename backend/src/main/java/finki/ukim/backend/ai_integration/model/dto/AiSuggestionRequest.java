package finki.ukim.backend.ai_integration.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiSuggestionRequest {

    @NotNull(message = "Request ID is required for AI categorization")
    private Long requestId;

    @NotBlank(message = "Title is required for AI categorization")
    private String title;

    private String description;
}
