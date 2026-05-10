package finki.ukim.backend.request_management.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.DisplayRequestLogDto;
import finki.ukim.backend.request_management.model.dto.filter.RequestLogFilterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequestLogController {


    @GetMapping("/request-logs/{logId}")
    public ResponseEntity<DisplayRequestLogDto> findById(
            @PathVariable Long logId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/requests/{requestId}/logs")
    public ResponseEntity<List<DisplayRequestLogDto>> findAllByRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/requests/{requestId}/logs/paged")
    public ResponseEntity<Page<DisplayRequestLogDto>> findAllByRequestPaged(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User user,
            @ModelAttribute RequestLogFilterDto filter
    ) {
        return ResponseEntity.ok().build();
    }
}
