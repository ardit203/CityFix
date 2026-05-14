package finki.ukim.backend.request_management.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.DisplayRequestLogBasicDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestLogDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestLogPageableDto;
import finki.ukim.backend.request_management.model.dto.filter.RequestLogFilterDto;
import finki.ukim.backend.request_management.service.application.RequestLogApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests/{requestId}/logs")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class RequestLogController {
    private final RequestLogApplicationService requestLogApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayRequestLogBasicDto>> findAllByRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity
                .ok(requestLogApplicationService.findAllByRequestId(requestId, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayRequestLogDto> findById(
            @PathVariable Long requestId,
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity
                .ok(requestLogApplicationService.findById(id, user));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<DisplayRequestLogPageableDto>> findAllByRequestPaged(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User user,
            @ModelAttribute RequestLogFilterDto filter
    ) {
        return ResponseEntity.ok(
                requestLogApplicationService.findAll(requestId, user, filter)
        );
    }
}
