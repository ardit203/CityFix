package finki.ukim.backend.request_management.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestAssignmentPageableDto;
import finki.ukim.backend.request_management.model.dto.filter.RequestAssignmentFilterDto;
import finki.ukim.backend.request_management.service.application.RequestAssignmentApplicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/request-assignments")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('ADMINISTRATOR', 'MANAGER', 'EMPLOYEE')")
public class RequestAssignmentManagementController {
    private final RequestAssignmentApplicationService requestAssignmentApplicationService;

    @GetMapping("/{assignmentId}")
    public ResponseEntity<DisplayRequestAssignmentDto> findById(
            @PathVariable Long assignmentId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                requestAssignmentApplicationService.findById(assignmentId, user)
        );
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<DisplayRequestAssignmentPageableDto>> findAll(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute RequestAssignmentFilterDto requestAssignmentFilterDto
    ) {
        return ResponseEntity.ok(
                requestAssignmentApplicationService.findAll(user, requestAssignmentFilterDto)
        );
    }
}
