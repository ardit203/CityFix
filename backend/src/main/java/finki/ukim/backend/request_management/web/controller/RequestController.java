package finki.ukim.backend.request_management.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.CreateRequestDto;
import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests")
@AllArgsConstructor
public class RequestController {

    @GetMapping
    public ResponseEntity<?> findAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/paged")
    public ResponseEntity<?> findAll(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) RequestStatus requestStatus,
            @RequestParam(required = false) Long municipalityId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long categoryId
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal User user, @Valid @RequestBody CreateRequestDto createRequestDto) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/timeline")
    public ResponseEntity<?> requestTimeline(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/timeline/paged")
    public ResponseEntity<?> requestTimeline(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/audit-log")
    public ResponseEntity<?> auditLog(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/ai-suggestion")
    public ResponseEntity<?> suggestion(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/ai-suggestion/approve")
    public ResponseEntity<?> approveSuggestion(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/ai-suggestion/reject")
    public ResponseEntity<?> rejectSuggestion(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/ai-suggestion/apply")
    public ResponseEntity<?> applySuggestion(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/routing")
    public ResponseEntity<?> routing(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/routing/confirm")
    public ResponseEntity<?> confirmRouting(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/routing/reject")
    public ResponseEntity<?> rejectRouting(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/routing/reopen")
    public ResponseEntity<?> reopenRouting(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }


}
