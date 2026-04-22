package finki.ukim.backend.file_handling.repository;

import finki.ukim.backend.file_handling.model.domain.File;
import finki.ukim.backend.file_handling.model.enums.FileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileName(String fileName);

    Optional<File> findByFileUrl(String fileUrl);

    List<File> findByFileType(FileType fileType);
}
