package finki.ukim.backend.request_management.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequestLogController {
//
//    private final RequestLogApplicationService requestLogApplicationService;
//
//    @GetMapping("/request-logs")
//    public Page<DisplayRequestLogDto> findAllPaged(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "desc") String sortDir,
//            @RequestParam(required = false) Long requestId,
//            @RequestParam(required = false) Long actionByUserId,
//            @RequestParam(required = false) RequestLogAction action
//    ) {
//        return requestLogApplicationService.findAllPaged(
//                page,
//                size,
//                sortBy,
//                sortDir,
//                requestId,
//                actionByUserId,
//                action
//        );
//    }
//
//    @GetMapping("/request-logs/{logId}")
//    public DisplayRequestLogDetailsDto findById(
//            @PathVariable Long logId
//    ) {
//        return requestLogApplicationService.findById(logId);
//    }
//
//    @GetMapping("/requests/{requestId}/logs")
//    public List<DisplayRequestLogDto> findAllByRequest(
//            @PathVariable Long requestId
//    ) {
//        return requestLogApplicationService.findAllByRequest(requestId);
//    }
//
//    @GetMapping("/requests/{requestId}/logs/paged")
//    public Page<DisplayRequestLogDto> findAllByRequestPaged(
//            @PathVariable Long requestId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "desc") String sortDir
//    ) {
//        return requestLogApplicationService.findAllByRequestPaged(
//                requestId,
//                page,
//                size,
//                sortBy,
//                sortDir
//        );
//    }
}
