package finki.ukim.backend.file_handling.model.exception;

import finki.ukim.backend.common.exception.ConflictException;
import finki.ukim.backend.file_handling.constants.FileConstants;
import finki.ukim.backend.file_handling.model.enums.FileType;

public class FileTypeNotAllowedException extends ConflictException {
    public FileTypeNotAllowedException(FileType fileType) {
        super(String.format(
                        "File type '%s' is not allowed. Allowed file types are: %s.",
                        fileType,
                        FileConstants.ALLOWED_FILE_TYPES
                )
        );
    }
}
