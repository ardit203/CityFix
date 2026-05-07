package finki.ukim.backend.request_management.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.CreateRequestDto;
import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import finki.ukim.backend.request_management.model.enums.RoutingStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/requests")
@AllArgsConstructor
public class RequestController {

    @GetMapping
    public ResponseEntity<?> findAll(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/paged")
    public ResponseEntity<?> findAllPaged(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) Long municipalityId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) RoutingStatus routingStatus,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) LocalDateTime submittedFrom,
            @RequestParam(required = false) LocalDateTime submittedTo
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody CreateRequestDto dto
    ) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<?> findById(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<?> cancel(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<?> deleteById(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok().build();
    }
}