package finki.ukim.backend.file_handling.service.domain;

import finki.ukim.backend.file_handling.model.domain.File;
import finki.ukim.backend.file_handling.model.enums.FileType;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

public interface FileService {
    List<File> findAll();

    Optional<File> findById(Long id);

    Optional<File> findByFileName(String fileName);

    Optional<File> findByFileUrl(String fileUrl);

    List<File> findByFileType(FileType fileType);

    File create(MultipartFile file, String directory);

    File create(MultipartFile file);

    List<File> createAll(List<MultipartFile> files, String directory);

    List<File> createAll(List<MultipartFile> files);

    Optional<File> update(Long id, MultipartFile file, String directory);

    Optional<File> update(Long id, MultipartFile file);

    Optional<File> deleteById(Long id);
}
