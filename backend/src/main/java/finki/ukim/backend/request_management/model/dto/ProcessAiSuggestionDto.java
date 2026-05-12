package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.enums.Priority;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessAiSuggestionDto {
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Priority is required")
    private Priority priority;

    private String summary;
}
