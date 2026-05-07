package finki.ukim.backend.request_management.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requests/{requestId}/routing")
@AllArgsConstructor
public class RequestRoutingController {
//
//    @PatchMapping
//    public ResponseEntity<?> updateRouting(
//            @PathVariable Long requestId,
//            @AuthenticationPrincipal User currentUser,
//            @RequestBody UpdateRequestRoutingDto dto
//    ) {
//        return ResponseEntity.ok().build();
//    }
//
//    @PatchMapping("/confirm")
//    public ResponseEntity<?> confirmRouting(
//            @PathVariable Long requestId,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        return ResponseEntity.ok().build();
//    }
//
//    @PatchMapping("/reject")
//    public ResponseEntity<?> rejectRouting(
//            @PathVariable Long requestId,
//            @AuthenticationPrincipal User currentUser,
//            @RequestBody RejectRoutingDto dto
//    ) {
//        return ResponseEntity.ok().build();
//    }
//
//    @PatchMapping("/reopen")
//    public ResponseEntity<?> reopenRouting(
//            @PathVariable Long requestId,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        return ResponseEntity.ok().build();
//    }
}
