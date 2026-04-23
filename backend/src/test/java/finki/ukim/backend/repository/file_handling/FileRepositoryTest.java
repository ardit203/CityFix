package finki.ukim.backend.repository.file_handling;

import finki.ukim.backend.common.config.JpaConfig;
import finki.ukim.backend.file_handling.model.domain.File;
import finki.ukim.backend.file_handling.model.enums.FileType;
import finki.ukim.backend.file_handling.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Testcontainers
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FileRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("cityfix_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private FileRepository fileRepository;

    private File pngFile;
    private File jpegFile;
    private File pdfFile;

    @BeforeEach
    void setUp() {
        pngFile  = fileRepository.save(new File("photo.png",   FileType.PNG,  "uploads/files/photo.png",   512_000L));
        jpegFile = fileRepository.save(new File("image.jpeg",  FileType.JPEG, "uploads/files/image.jpeg",  256_000L));
        pdfFile  = fileRepository.save(new File("report.pdf",  FileType.PDF,  "uploads/files/report.pdf", 1_024_000L));
    }

    @Test
    void findByFileName_shouldReturnFile_whenNameExists() {
        Optional<File> result = fileRepository.findByFileName("photo.png");

        assertThat(result).isPresent();
        assertThat(result.get().getFileType()).isEqualTo(FileType.PNG);
        assertThat(result.get().getFileSize()).isEqualTo(512_000L);
    }

    @Test
    void findByFileName_shouldReturnEmpty_whenNameDoesNotExist() {
        Optional<File> result = fileRepository.findByFileName("nonexistent.png");

        assertThat(result).isEmpty();
    }

    @Test
    void findByFileUrl_shouldReturnFile_whenUrlExists() {
        Optional<File> result = fileRepository.findByFileUrl("uploads/files/image.jpeg");

        assertThat(result).isPresent();
        assertThat(result.get().getFileName()).isEqualTo("image.jpeg");
    }

    @Test
    void findByFileUrl_shouldReturnEmpty_whenUrlDoesNotExist() {
        Optional<File> result = fileRepository.findByFileUrl("uploads/files/missing.pdf");

        assertThat(result).isEmpty();
    }

    @Test
    void findByFileType_shouldReturnOnlyMatchingFiles() {
        List<File> results = fileRepository.findByFileType(FileType.PNG);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFileName()).isEqualTo("photo.png");
    }

    @Test
    void findByFileType_shouldReturnMultipleFiles_whenSeveralMatchType() {
        fileRepository.save(new File("photo2.png", FileType.PNG, "uploads/files/photo2.png", 128_000L));

        List<File> results = fileRepository.findByFileType(FileType.PNG);

        assertThat(results).hasSize(2);
    }

    @Test
    void findByFileType_shouldReturnEmpty_whenNoFilesMatchType() {
        List<File> results = fileRepository.findByFileType(FileType.XLSX);

        assertThat(results).isEmpty();
    }

    @Test
    void save_shouldThrowException_whenDuplicateFileNameIsInserted() {
        assertThatThrownBy(() ->
                fileRepository.saveAndFlush(new File("photo.png", FileType.PNG, "uploads/files/different.png", 1L))
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_shouldThrowException_whenDuplicateFileUrlIsInserted() {
        assertThatThrownBy(() ->
                fileRepository.saveAndFlush(new File("other.png", FileType.PNG, "uploads/files/photo.png", 1L))
        ).isInstanceOf(DataIntegrityViolationException.class);
    }
}
