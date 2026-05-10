package finki.ukim.backend.request_management.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.*;
import finki.ukim.backend.request_management.model.dto.filter.RequestFilterDto;
import finki.ukim.backend.request_management.service.application.RequestApplicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@AllArgsConstructor
public class RequestController {
    private final RequestApplicationService requestApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayBasicRequestDto>> findAll(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(requestApplicationService.findAll(currentUser));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<DisplayRequestPageableDto>> findAllPaged(
            @AuthenticationPrincipal User currentUser,
            @ModelAttribute RequestFilterDto requestFilterDto
    ) {
        return ResponseEntity.ok(
                requestApplicationService
                        .findAll(currentUser, requestFilterDto)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<DisplayRequestDto> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(
                requestApplicationService.findById(id, currentUser)
        );
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DisplayRequestDto> create(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestPart("request") CreateRequestDto createRequestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return ResponseEntity.ok(
                requestApplicationService.create(currentUser, createRequestDto, files)
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(requestApplicationService.cancel(id, currentUser));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DisplayRequestDto> changeStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser,
            @RequestBody ChangeRequestStatusDto changeRequestStatusDto
    ) {
        return ResponseEntity.ok(
                requestApplicationService.changeStatus(id, currentUser, changeRequestStatusDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {
        requestApplicationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> deleteBulk(
            @RequestBody List<Long> ids
    ) {
        requestApplicationService.deleteBulk(ids);
        return ResponseEntity.ok().build();
    }
}