package finki.ukim.backend.request_management.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.DisplayRequestDto;
import finki.ukim.backend.request_management.model.dto.RejectRoutingDto;
import finki.ukim.backend.request_management.model.dto.UpdateRequestRoutingDto;
import finki.ukim.backend.request_management.service.application.RequestRoutingApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests/{requestId}/routing")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'MANAGER')")
public class RequestRoutingController {

    private final RequestRoutingApplicationService requestRoutingApplicationService;

    @PatchMapping
    public ResponseEntity<DisplayRequestDto> updateRouting(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser,
            @RequestBody UpdateRequestRoutingDto dto
    ) {
        return ResponseEntity.ok(requestRoutingApplicationService.updateRouting(requestId, currentUser, dto));
    }

    @PatchMapping("/confirm")
    public ResponseEntity<DisplayRequestDto> confirmRouting(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(requestRoutingApplicationService.confirmRouting(requestId, currentUser));
    }

    @PatchMapping("/reject")
    public ResponseEntity<DisplayRequestDto> rejectRouting(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser,
            @RequestBody RejectRoutingDto dto
    ) {
        return ResponseEntity.ok(requestRoutingApplicationService.rejectRouting(requestId, currentUser, dto));
    }

    @PatchMapping("/reopen")
    public ResponseEntity<DisplayRequestDto> reopenRouting(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(requestRoutingApplicationService.reopenRouting(requestId, currentUser));
    }
}
