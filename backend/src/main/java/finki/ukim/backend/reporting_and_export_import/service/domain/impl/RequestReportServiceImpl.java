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

        Long totalRequests = requestRepository.countFiltered(
                scope.requestedUserId(),
                scope.departmentId(),
                scope.municipalityId(),
                filter.getCategoryId(),
                filter.getStatus(),
                filter.getRoutingStatus(),
                filter.getPriority(),
                filter.getText(),
                filter.getSubmittedFrom(),
                filter.getSubmittedTo()
        );

        Long assignedRequests = requestRepository.countAssignedFiltered(
                scope.requestedUserId(),
                scope.departmentId(),
                scope.municipalityId(),
                filter.getCategoryId(),
                filter.getStatus(),
                filter.getRoutingStatus(),
                filter.getPriority(),
                filter.getText(),
                filter.getSubmittedFrom(),
                filter.getSubmittedTo()
        );

        return new RequestReportSummaryDto(
                totalRequests,
                totalRequests - assignedRequests,
                assignedRequests,
                mapCounts(requestRepository.countByStatus(
                        scope.requestedUserId(), scope.departmentId(), scope.municipalityId(),
                        filter.getCategoryId(), filter.getStatus(), filter.getRoutingStatus(), filter.getPriority(),
                        filter.getText(), filter.getSubmittedFrom(), filter.getSubmittedTo()
                )),
                mapCounts(requestRepository.countByPriority(
                        scope.requestedUserId(), scope.departmentId(), scope.municipalityId(),
                        filter.getCategoryId(), filter.getStatus(), filter.getRoutingStatus(), filter.getPriority(),
                        filter.getText(), filter.getSubmittedFrom(), filter.getSubmittedTo()
                )),
                mapCounts(requestRepository.countByRoutingStatus(
                        scope.requestedUserId(), scope.departmentId(), scope.municipalityId(),
                        filter.getCategoryId(), filter.getStatus(), filter.getRoutingStatus(), filter.getPriority(),
                        filter.getText(), filter.getSubmittedFrom(), filter.getSubmittedTo()
                )),
                mapCounts(requestRepository.countByDepartment(
                        scope.requestedUserId(), scope.departmentId(), scope.municipalityId(),
                        filter.getCategoryId(), filter.getStatus(), filter.getRoutingStatus(), filter.getPriority(),
                        filter.getText(), filter.getSubmittedFrom(), filter.getSubmittedTo()
                )),
                mapCounts(requestRepository.countByMunicipality(
                        scope.requestedUserId(), scope.departmentId(), scope.municipalityId(),
                        filter.getCategoryId(), filter.getStatus(), filter.getRoutingStatus(), filter.getPriority(),
                        filter.getText(), filter.getSubmittedFrom(), filter.getSubmittedTo()
                )),
                mapCounts(requestRepository.countByCategory(
                        scope.requestedUserId(), scope.departmentId(), scope.municipalityId(),
                        filter.getCategoryId(), filter.getStatus(), filter.getRoutingStatus(), filter.getPriority(),
                        filter.getText(), filter.getSubmittedFrom(), filter.getSubmittedTo()
                ))
        );
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
