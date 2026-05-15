package finki.ukim.backend.reporting_and_export_import.service.domain.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.RequestScopeFilters;
import finki.ukim.backend.auth_and_access.service.domain.AccessScopeService;
import finki.ukim.backend.reporting_and_export_import.model.dto.ReportCountDto;
import finki.ukim.backend.reporting_and_export_import.model.dto.RequestReportSummaryDto;
import finki.ukim.backend.reporting_and_export_import.service.domain.RequestReportService;
import finki.ukim.backend.request_management.model.dto.filter.RequestFilterDto;
import finki.ukim.backend.request_management.repository.RequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestReportServiceImpl implements RequestReportService {
    private final RequestRepository requestRepository;
    private final AccessScopeService accessScopeService;

    @Override
    @Transactional(readOnly = true)
    public RequestReportSummaryDto getRequestSummary(User currentUser, RequestFilterDto filter) {
        filter.normalizeTextFields();

        RequestScopeFilters scope = accessScopeService.getRequestFilters(
                currentUser,
                filter.getCitizenId(),
                filter.getDepartmentId(),
                filter.getMunicipalityId(),
                filter.getAssignedEmployeeUserId()
        );

        String text = normalizeText(filter.getText());
        LocalDateTime submittedFrom = normalizeSubmittedFrom(filter.getSubmittedFrom());
        LocalDateTime submittedTo = normalizeSubmittedTo(filter.getSubmittedTo());

        Long totalRequests = requestRepository.countFiltered(
                scope.requestedUserId(),
                scope.departmentId(),
                scope.municipalityId(),
                filter.getCategoryId(),
                filter.getStatus(),
                filter.getRoutingStatus(),
                filter.getPriority(),
                text,
                submittedFrom,
                submittedTo
        );

        Long assignedRequests = requestRepository.countAssignedFiltered(
                scope.requestedUserId(),
                scope.departmentId(),
                scope.municipalityId(),
                filter.getCategoryId(),
                filter.getStatus(),
                filter.getRoutingStatus(),
                filter.getPriority(),
                text,
                submittedFrom,
                submittedTo
        );

        totalRequests = safeCount(totalRequests);
        assignedRequests = safeCount(assignedRequests);

        return new RequestReportSummaryDto(
                totalRequests,
                totalRequests - assignedRequests,
                assignedRequests,
                mapCounts(requestRepository.countByStatus(
                        scope.requestedUserId(),
                        scope.departmentId(),
                        scope.municipalityId(),
                        filter.getCategoryId(),
                        filter.getStatus(),
                        filter.getRoutingStatus(),
                        filter.getPriority(),
                        text,
                        submittedFrom,
                        submittedTo
                )),
                mapCounts(requestRepository.countByPriority(
                        scope.requestedUserId(),
                        scope.departmentId(),
                        scope.municipalityId(),
                        filter.getCategoryId(),
                        filter.getStatus(),
                        filter.getRoutingStatus(),
                        filter.getPriority(),
                        text,
                        submittedFrom,
                        submittedTo
                )),
                mapCounts(requestRepository.countByRoutingStatus(
                        scope.requestedUserId(),
                        scope.departmentId(),
                        scope.municipalityId(),
                        filter.getCategoryId(),
                        filter.getStatus(),
                        filter.getRoutingStatus(),
                        filter.getPriority(),
                        text,
                        submittedFrom,
                        submittedTo
                )),
                mapCounts(requestRepository.countByDepartment(
                        scope.requestedUserId(),
                        scope.departmentId(),
                        scope.municipalityId(),
                        filter.getCategoryId(),
                        filter.getStatus(),
                        filter.getRoutingStatus(),
                        filter.getPriority(),
                        text,
                        submittedFrom,
                        submittedTo
                )),
                mapCounts(requestRepository.countByMunicipality(
                        scope.requestedUserId(),
                        scope.departmentId(),
                        scope.municipalityId(),
                        filter.getCategoryId(),
                        filter.getStatus(),
                        filter.getRoutingStatus(),
                        filter.getPriority(),
                        text,
                        submittedFrom,
                        submittedTo
                )),
                mapCounts(requestRepository.countByCategory(
                        scope.requestedUserId(),
                        scope.departmentId(),
                        scope.municipalityId(),
                        filter.getCategoryId(),
                        filter.getStatus(),
                        filter.getRoutingStatus(),
                        filter.getPriority(),
                        text,
                        submittedFrom,
                        submittedTo
                ))
        );
    }

    private String normalizeText(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }

        return text.toLowerCase();
    }

    private LocalDateTime normalizeSubmittedFrom(LocalDateTime submittedFrom) {
        if (submittedFrom == null) {
            return LocalDateTime.of(1900, 1, 1, 0, 0);
        }

        return submittedFrom;
    }

    private LocalDateTime normalizeSubmittedTo(LocalDateTime submittedTo) {
        if (submittedTo == null) {
            return LocalDateTime.of(9999, 12, 31, 23, 59, 59);
        }

        return submittedTo;
    }

    private Long safeCount(Long count) {
        return count == null ? 0L : count;
    }

    private List<ReportCountDto> mapCounts(List<Object[]> rows) {
        return rows.stream()
                .map(row -> new ReportCountDto(
                        row[0] == null ? "Unassigned" : row[0].toString(),
                        (Long) row[1]
                ))
                .toList();
    }
}
