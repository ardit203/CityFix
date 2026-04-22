package finki.ukim.backend.file_handling.model.exception;

import finki.ukim.backend.common.exception.ConflictException;
import finki.ukim.backend.file_handling.constants.FileConstants;

public class FileSizeExceededException extends ConflictException {
    public FileSizeExceededException() {
        super("File size exceeds the maximum allowed limit. The maximum file size is " + FileConstants.getMaxFileSizeInMB() + " MB.");
    }

    public FileSizeExceededException(int count) {
        super("The total size of the " + count + " files exceeds the maximum allowed limit. The maximum file size is " + FileConstants.getMaxFileSizeInMB() + " MB.");
    }
}
