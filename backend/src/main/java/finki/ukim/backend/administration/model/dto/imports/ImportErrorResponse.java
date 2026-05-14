package finki.ukim.backend.administration.model.dto.imports;

import java.util.List;

public record ImportErrorResponse(
        String message,
        List<ImportCellError> errors
) {
}
