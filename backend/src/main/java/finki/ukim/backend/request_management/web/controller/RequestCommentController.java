package finki.ukim.backend.request_management.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requests/{requestId}/comments")
@RequiredArgsConstructor
public class RequestCommentController {
//
//    private final RequestCommentApplicationService requestCommentApplicationService;
//
//    @GetMapping
//    public List<DisplayRequestCommentDto> findAllByRequest(
//            @PathVariable Long requestId
//    ) {
//        return requestCommentApplicationService.findAllByRequest(requestId);
//    }
//
//    @PostMapping
//    public DisplayRequestCommentDto create(
//            @PathVariable Long requestId,
//            @RequestBody CreateRequestCommentDto dto
//    ) {
//        return requestCommentApplicationService.create(requestId, dto);
//    }
//
//    @PutMapping("/{commentId}")
//    public DisplayRequestCommentDto update(
//            @PathVariable Long requestId,
//            @PathVariable Long commentId,
//            @RequestBody UpdateRequestCommentDto dto
//    ) {
//        return requestCommentApplicationService.update(requestId, commentId, dto);
//    }
//
//    @DeleteMapping("/{commentId}")
//    public DisplayRequestCommentDto delete(
//            @PathVariable Long requestId,
//            @PathVariable Long commentId
//    ) {
//        return requestCommentApplicationService.delete(requestId, commentId);
//    }
}
