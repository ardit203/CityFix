package finki.ukim.backend.file_handling.model.exception;

import finki.ukim.backend.common.exception.ConflictException;

public class FileEmptyException extends ConflictException {
    public FileEmptyException() {
        super("File cannot be empty.");
    }
}
