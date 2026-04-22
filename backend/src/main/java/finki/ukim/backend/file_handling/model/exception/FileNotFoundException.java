package finki.ukim.backend.file_handling.model.exception;

import finki.ukim.backend.common.exception.ResourceNotFoundException;

public class FileNotFoundException extends ResourceNotFoundException {
    public FileNotFoundException(String fileUrl) {
        super(String.format("File with URL '%s' does not exist.", fileUrl));
    }

        public FileNotFoundException(Long id) {
            super(String.format("File with ID '%d' does not exist.", id));
        }
}
