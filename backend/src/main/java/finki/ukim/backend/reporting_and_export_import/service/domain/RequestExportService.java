package finki.ukim.backend.reporting_and_export_import.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;

public interface RequestExportService {
    byte[] exportRequestAsPdf(Long requestId, User currentUser);
}
