package finki.ukim.backend.file_handling.service.domain;

import finki.ukim.backend.file_handling.model.domain.File;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface FileStorageService {
    File save(MultipartFile file, String directory);

    List<File> saveAll(List<MultipartFile> files, String directory);

    boolean delete(String fileUrl);

    Path load(String fileUrl);

    boolean exists(String fileUrl);

    void validateFile(MultipartFile file);

    void validateFiles(List<MultipartFile> files);
}
