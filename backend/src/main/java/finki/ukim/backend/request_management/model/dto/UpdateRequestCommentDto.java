package finki.ukim.backend.request_management.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateRequestCommentDto(
        @NotBlank(message = "Comment content is required")
        @Size(max = 2000, message = "Comment must not be longer than 2000 characters")
        String content,

        @NotNull(message = "Internal flag is required")
        Boolean internal
) {
}