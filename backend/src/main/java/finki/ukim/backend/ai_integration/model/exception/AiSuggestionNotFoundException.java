package finki.ukim.backend.ai_integration.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class AiSuggestionNotFoundException extends ResourceNotFoundException {

    public AiSuggestionNotFoundException(Long requestId) {
        super(String.format("Suggestion with request id '%s' was not found!", requestId));
    }
}
