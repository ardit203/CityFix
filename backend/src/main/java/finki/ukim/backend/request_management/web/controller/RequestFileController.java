package finki.ukim.backend.request_management.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requests/{requestId}/files")
@AllArgsConstructor
public class RequestFileController {
//
//    @GetMapping
//    public ResponseEntity<?> findAllByRequest(
//            @PathVariable Long requestId,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> uploadFiles(
//            @PathVariable Long requestId,
//            @AuthenticationPrincipal User currentUser,
//            @RequestPart("files") List<MultipartFile> files
//    ) {
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/{fileId}")
//    public ResponseEntity<?> deleteFile(
//            @PathVariable Long requestId,
//            @PathVariable Long fileId,
//            @AuthenticationPrincipal User currentUser
//    ) {
//        return ResponseEntity.ok().build();
//    }
}

//maximum 5 files per request
//allowed types: JPG, PNG, PDF
//citizen can upload during request creation or before request is processed
//staff/admin can view files based on access rules
