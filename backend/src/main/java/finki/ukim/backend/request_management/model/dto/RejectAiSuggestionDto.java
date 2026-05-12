package finki.ukim.backend.request_management.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectAiSuggestionDto {
    @NotBlank(message = "Rejection reason is required")
    private String reason;
}
