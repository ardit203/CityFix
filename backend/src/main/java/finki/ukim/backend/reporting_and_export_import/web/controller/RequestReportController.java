package finki.ukim.backend.reporting_and_export_import.web.controller;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.reporting_and_export_import.model.dto.RequestReportSummaryDto;
import finki.ukim.backend.reporting_and_export_import.service.domain.RequestReportService;
import finki.ukim.backend.request_management.model.dto.filter.RequestFilterDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class RequestReportController {
    private final RequestReportService requestReportService;

    @GetMapping("/requests/summary")
    public ResponseEntity<RequestReportSummaryDto> getRequestSummary(
            @AuthenticationPrincipal User currentUser,
            @Valid @ModelAttribute RequestFilterDto filter
    ) {
        return ResponseEntity.ok(
                requestReportService.getRequestSummary(currentUser, filter)
        );
    }
}
