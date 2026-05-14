package finki.ukim.backend.reporting_and_export_import.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.model.dto.imports.ImportCellError;
import finki.ukim.backend.administration.model.exception.InsufficientRoleException;
import finki.ukim.backend.administration.model.exception.StaffImportException;
import finki.ukim.backend.administration.repository.DepartmentRepository;
import finki.ukim.backend.administration.repository.MunicipalityRepository;
import finki.ukim.backend.administration.repository.StaffRepository;
import finki.ukim.backend.reporting_and_export_import.service.domain.StaffImportService;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.repository.UserRepository;
import finki.ukim.backend.auth_and_access.service.domain.AccessScopeService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class StaffImportServiceImpl implements StaffImportService {
    private static final int USER_ID_COL = 0;
    private static final int DEPARTMENT_ID_COL = 1;
    private static final int MUNICIPALITY_ID_COL = 2;

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final MunicipalityRepository municipalityRepository;
    private final AccessScopeService accessScopeService;

    @Override
    @Transactional
    public int importStaffFromExcel(MultipartFile file, User currentUser) {
        if (!accessScopeService.isAdmin(currentUser)) {
            throw new InsufficientRoleException(currentUser.getUsername());
        }

        List<ImportCellError> errors = new ArrayList<>();
        List<Staff> staffToSave = new ArrayList<>();
        Set<Long> userIdsInFile = new HashSet<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            if (workbook.getNumberOfSheets() == 0) {
                throw new StaffImportException(List.of(
                        new ImportCellError(1, "file", "Excel file must contain at least one sheet")
                ));
            }

            Sheet sheet = workbook.getSheetAt(0);
            validateHeader(sheet, errors);

            if (!errors.isEmpty()) {
                throw new StaffImportException(errors);
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                int excelRowNumber = i + 1;
                boolean rowHasErrors = false;

                Long userId = readLongCell(row, USER_ID_COL, "userId", excelRowNumber, errors);
                Long departmentId = readLongCell(row, DEPARTMENT_ID_COL, "departmentId", excelRowNumber, errors);
                Long municipalityId = readLongCell(row, MUNICIPALITY_ID_COL, "municipalityId", excelRowNumber, errors);

                if (userId == null || departmentId == null || municipalityId == null) {
                    continue;
                }

                if (!userIdsInFile.add(userId)) {
                    errors.add(new ImportCellError(
                            excelRowNumber,
                            "userId",
                            "Duplicate userId inside Excel file"
                    ));
                    rowHasErrors = true;
                }

                Optional<User> userOptional = userRepository.findById(userId);
                Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
                Optional<Municipality> municipalityOptional = municipalityRepository.findById(municipalityId);

                if (userOptional.isEmpty()) {
                    errors.add(new ImportCellError(
                            excelRowNumber,
                            "userId",
                            "User with id " + userId + " does not exist"
                    ));
                    rowHasErrors = true;
                }

                if (departmentOptional.isEmpty()) {
                    errors.add(new ImportCellError(
                            excelRowNumber,
                            "departmentId",
                            "Department with id " + departmentId + " does not exist"
                    ));
                    rowHasErrors = true;
                }

                if (municipalityOptional.isEmpty()) {
                    errors.add(new ImportCellError(
                            excelRowNumber,
                            "municipalityId",
                            "Municipality with id " + municipalityId + " does not exist"
                    ));
                    rowHasErrors = true;
                }

                if (userOptional.isPresent() && staffRepository.existsByUser_Id(userId)) {
                    errors.add(new ImportCellError(
                            excelRowNumber,
                            "userId",
                            "User with id " + userId + " is already staff"
                    ));
                    rowHasErrors = true;
                }

                if (userOptional.isPresent() && accessScopeService.isCitizen(userOptional.get())) {
                    errors.add(new ImportCellError(
                            excelRowNumber,
                            "userId",
                            "Citizen users cannot be assigned as staff"
                    ));
                    rowHasErrors = true;
                }

                if (rowHasErrors) {
                    continue;
                }

                Staff staff = new Staff();
                staff.setUser(userOptional.get());
                staff.setDepartment(departmentOptional.get());
                staff.setMunicipality(municipalityOptional.get());
                staffToSave.add(staff);
            }

            if (!errors.isEmpty()) {
                throw new StaffImportException(errors);
            }

            try {
                staffRepository.saveAll(staffToSave);
                staffRepository.flush();
                return staffToSave.size();
            } catch (DataIntegrityViolationException e) {
                throw new StaffImportException(List.of(
                        new ImportCellError(
                                -1,
                                "userId",
                                "Database constraint failed. One or more users are already staff. No staff members were saved."
                        )
                ));
            }
        } catch (StaffImportException e) {
            throw e;
        } catch (Exception e) {
            throw new StaffImportException(List.of(
                    new ImportCellError(1, "file", "Failed to read Excel file")
            ));
        }
    }

    private void validateHeader(Sheet sheet, List<ImportCellError> errors) {
        Row header = sheet.getRow(0);

        if (header == null) {
            errors.add(new ImportCellError(1, "header", "Excel file must contain a header row"));
            return;
        }

        validateHeaderCell(header, USER_ID_COL, "userId", errors);
        validateHeaderCell(header, DEPARTMENT_ID_COL, "departmentId", errors);
        validateHeaderCell(header, MUNICIPALITY_ID_COL, "municipalityId", errors);
    }

    private void validateHeaderCell(Row header, int columnIndex, String expectedName, List<ImportCellError> errors) {
        DataFormatter formatter = new DataFormatter();
        Cell cell = header.getCell(columnIndex);
        String actualValue = cell != null ? formatter.formatCellValue(cell).trim() : "";

        if (!expectedName.equalsIgnoreCase(actualValue)) {
            errors.add(new ImportCellError(
                    1,
                    expectedName,
                    "Expected header '" + expectedName + "'"
            ));
        }
    }

    private Long readLongCell(
            Row row,
            int columnIndex,
            String columnName,
            int excelRowNumber,
            List<ImportCellError> errors
    ) {
        Cell cell = row.getCell(columnIndex);

        if (cell == null || cell.getCellType() == CellType.BLANK) {
            errors.add(new ImportCellError(excelRowNumber, columnName, "Value is required"));
            return null;
        }

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                double value = cell.getNumericCellValue();

                if (value % 1 != 0) {
                    errors.add(new ImportCellError(excelRowNumber, columnName, "Value must be a whole number"));
                    return null;
                }

                return (long) value;
            }

            if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();

                if (value.isBlank()) {
                    errors.add(new ImportCellError(excelRowNumber, columnName, "Value is required"));
                    return null;
                }

                return Long.parseLong(value);
            }

            errors.add(new ImportCellError(excelRowNumber, columnName, "Value must be a number"));
            return null;
        } catch (NumberFormatException e) {
            errors.add(new ImportCellError(excelRowNumber, columnName, "Value must be a valid number"));
            return null;
        }
    }

    private boolean isRowEmpty(Row row) {
        for (int i = 0; i <= MUNICIPALITY_ID_COL; i++) {
            Cell cell = row.getCell(i);

            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }

        return true;
    }
}
