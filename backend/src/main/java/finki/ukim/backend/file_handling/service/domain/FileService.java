package finki.ukim.backend.file_handling.service.domain;

import finki.ukim.backend.file_handling.model.domain.File;
import finki.ukim.backend.file_handling.model.enums.FileType;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

public interface FileService {
    List<File> findAll();

    File findById(Long id);

    File findByFileUrl(String fileUrl);

    List<File> findByFileType(FileType fileType);

    File create(MultipartFile file, String directory);

    File create(MultipartFile file);

    List<File> createAll(List<MultipartFile> files, String directory);

    List<File> createAll(List<MultipartFile> files);

    File update(Long id, MultipartFile file, String directory);

    File update(Long id, MultipartFile file);

    File deleteById(Long id);
}
