package finki.ukim.backend.file_handling.model.exception;

import finki.ukim.backend.common.exception.ConflictException;
import finki.ukim.backend.file_handling.constants.FileConstants;

public class FileCountExceededException extends ConflictException {
    public FileCountExceededException(Integer count) {
        super(
                String.format(
                        "The number of files you uploaded (%d) exceeds the maximum allowed limit of %d files.",
                        count,
                        FileConstants.MAX_FILES_COUNT
                )
        );
    }
}
