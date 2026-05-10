package finki.ukim.backend.request_management.web.controller;

import finki.ukim.backend.request_management.model.dto.CreateRequestCommentDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestCommentDto;
import finki.ukim.backend.request_management.model.dto.UpdateRequestCommentDto;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.service.application.RequestCommentApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests/{requestId}/comments")
@RequiredArgsConstructor
public class RequestCommentController {

    private final RequestCommentApplicationService requestCommentApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayRequestCommentDto>> findAllByRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(requestCommentApplicationService.findAllByRequest(requestId, currentUser));
    }

    @PostMapping
    public ResponseEntity<DisplayRequestCommentDto> create(
            @PathVariable Long requestId,
            @RequestBody CreateRequestCommentDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(requestCommentApplicationService.create(requestId, dto, currentUser));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<DisplayRequestCommentDto> update(
            @PathVariable Long requestId,
            @PathVariable Long commentId,
            @RequestBody UpdateRequestCommentDto dto,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(requestCommentApplicationService.update(requestId, commentId, dto, currentUser));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<DisplayRequestCommentDto> delete(
            @PathVariable Long requestId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(requestCommentApplicationService.delete(requestId, commentId, currentUser));
    }
}
