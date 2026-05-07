package finki.ukim.backend.request_management.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requests/{requestId}/ai-suggestion")
@AllArgsConstructor
public class RequestAISuggestionController {
//
//    @GetMapping
//    public ResponseEntity<?> findSuggestion(
//            @PathVariable Long requestId,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/generate")
//    public ResponseEntity<?> generateSuggestion(
//            @PathVariable Long requestId,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/approve")
//    public ResponseEntity<?> approveSuggestion(
//            @PathVariable Long requestId,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/reject")
//    public ResponseEntity<?> rejectSuggestion(
//            @PathVariable Long requestId,
//            @AuthenticationPrincipal User currentUser,
//            @RequestBody RejectAiSuggestionDto dto
//    ) {
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/apply")
//    public ResponseEntity<?> applySuggestion(
//            @PathVariable Long requestId,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        return ResponseEntity.ok().build();
//    }
}
