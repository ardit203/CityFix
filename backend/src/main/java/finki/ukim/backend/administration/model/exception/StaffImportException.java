package finki.ukim.backend.administration.model.exception;

import finki.ukim.backend.administration.model.dto.imports.ImportCellError;

import java.util.List;

public class StaffImportException extends RuntimeException {
    private final List<ImportCellError> errors;

    public StaffImportException(List<ImportCellError> errors) {
        super("Staff import failed");
        this.errors = errors;
    }

    public List<ImportCellError> getErrors() {
        return errors;
    }
}
