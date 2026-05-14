package finki.ukim.backend.reporting_and_export_import.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.reporting_and_export_import.model.dto.RequestReportSummaryDto;
import finki.ukim.backend.request_management.model.dto.filter.RequestFilterDto;

public interface RequestReportService {
    RequestReportSummaryDto getRequestSummary(User currentUser, RequestFilterDto filter);
}
