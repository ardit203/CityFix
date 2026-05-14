package finki.ukim.backend.reporting_and_export_import.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import org.springframework.web.multipart.MultipartFile;

public interface StaffImportService {
    int importStaffFromExcel(MultipartFile file, User currentUser);
}
