package finki.ukim.backend.request_management.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requests/{requestId}/status")
@AllArgsConstructor
public class RequestStatusController {

    @PatchMapping
    public ResponseEntity<?> changeStatus(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User currentUser
//            @RequestBody ChangeRequestStatusDto dto
    ) {
        return ResponseEntity.ok().build();
    }
}