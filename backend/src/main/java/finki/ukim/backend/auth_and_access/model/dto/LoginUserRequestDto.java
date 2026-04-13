package finki.ukim.backend.auth_and_access.model.dto;

public record LoginUserRequestDto(
        String username,
        String password
) {
}