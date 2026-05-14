package finki.ukim.backend.administration.model.dto.imports;

public record ImportCellError(
        int row,
        String column,
        String message
) {
}
