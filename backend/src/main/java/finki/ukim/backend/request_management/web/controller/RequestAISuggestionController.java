package finki.ukim.backend.request_management.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.ProcessAiSuggestionDto;
import finki.ukim.backend.request_management.model.dto.RejectAiSuggestionDto;
import finki.ukim.backend.request_management.service.application.RequestAiSuggestionApplicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests/{requestId}/ai-suggestion")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'MANAGER')")
public class RequestAISuggestionController {

    private final RequestAiSuggestionApplicationService requestAiSuggestionAppService;

    @GetMapping
    public ResponseEntity<?> findSuggestion(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(requestAiSuggestionAppService.findSuggestion(requestId, currentUser));
    }

    @PostMapping("/process")
    public ResponseEntity<?> processSuggestion(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser,
            @RequestBody @Valid ProcessAiSuggestionDto dto
    ) {
        requestAiSuggestionAppService.processSuggestion(requestId, currentUser, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<?> rejectSuggestion(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser,
            @RequestBody @Valid RejectAiSuggestionDto dto
    ) {
        requestAiSuggestionAppService.rejectSuggestion(requestId, currentUser, dto);
        return ResponseEntity.ok().build();
    }
}
