package finki.ukim.backend.file_handling.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class FileErrorException extends ConflictException {
    public FileErrorException() {
        super("An error occurred while processing the file.");
    }

    public FileErrorException(String message) {
        super(message);
    }
}
