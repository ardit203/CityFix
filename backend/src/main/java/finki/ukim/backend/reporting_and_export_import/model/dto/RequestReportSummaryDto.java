package finki.ukim.backend.reporting_and_export_import.model.dto;

import java.util.List;

public record RequestReportSummaryDto(
        Long totalRequests,
        Long unassignedRequests,
        Long assignedRequests,
        List<ReportCountDto> byStatus,
        List<ReportCountDto> byPriority,
        List<ReportCountDto> byRoutingStatus,
        List<ReportCountDto> byDepartment,
        List<ReportCountDto> byMunicipality,
        List<ReportCountDto> byCategory
) {
}
