package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.domain.Request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateRequestDto(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not be longer than 255 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(max = 2000, message = "Description must not be longer than 2000 characters")
        String description,

        @NotNull(message = "User id is required")
        Long userId,

        @NotNull(message = "Municipality id is required")
        Long municipalityId,

        @NotNull(message = "Request location is required")
        @Valid
        RequestLocationDto requestLocationDto
) {
    public Request toRequest(User user, Municipality municipality) {
        return new Request(
                title,
                description,
                user,
                municipality,
                requestLocationDto.toRequestLocation()
        );
    }
}