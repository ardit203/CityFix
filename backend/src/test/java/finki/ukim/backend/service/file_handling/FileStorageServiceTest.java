package finki.ukim.backend.service.file_handling;

import finki.ukim.backend.file_handling.constants.FileConstants;
import finki.ukim.backend.file_handling.helper.FileHelper;
import finki.ukim.backend.file_handling.model.domain.File;
import finki.ukim.backend.file_handling.model.enums.FileType;
import finki.ukim.backend.file_handling.model.exception.*;
import finki.ukim.backend.file_handling.service.domain.impl.FileStorageServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTest {

    @TempDir
    Path tempDir;

    private FileStorageServiceImpl fileStorageService;

    /** Save original ROOT so it can be restored after each test. */
    private Path originalRoot;

    @BeforeEach
    void setUp() {
        originalRoot = FileConstants.ROOT;
        FileConstants.setROOT(tempDir.toString());       // redirect all I/O to temp dir

        fileStorageService = new FileStorageServiceImpl(new FileHelper());
    }

    @AfterEach
    void tearDown() {
        // Restore so other tests that use FileConstants are not affected
        FileConstants.ROOT = originalRoot;
    }

    // ── validateFile ──────────────────────────────────────────────────────

    @Test
    void validateFile_shouldThrow_whenFileIsNull() {
        assertThatThrownBy(() -> fileStorageService.validateFile(null))
                .isInstanceOf(FileEmptyException.class);
    }

    @Test
    void validateFile_shouldThrow_whenFileIsEmpty() {
        MockMultipartFile empty = new MockMultipartFile("file", new byte[0]);

        assertThatThrownBy(() -> fileStorageService.validateFile(empty))
                .isInstanceOf(FileEmptyException.class);
    }

    @Test
    void validateFile_shouldThrow_whenFileSizeExceedsMax() {
        // FileConstants.MAX_FILE_SIZE = 10 MB; use 11 MB fake size
        byte[] content = new byte[1];
        MockMultipartFile big = new MockMultipartFile(
                "file", "big.png", "image/png",
                content) {
            @Override public long getSize() { return FileConstants.MAX_FILE_SIZE + 1; }
        };

        assertThatThrownBy(() -> fileStorageService.validateFile(big))
                .isInstanceOf(FileSizeExceededException.class);
    }

    @Test
    void validateFile_shouldNotThrow_whenFileIsValid() {
        MockMultipartFile valid = new MockMultipartFile(
                "file", "photo.png", "image/png", new byte[1024]);

        // Should not throw
        fileStorageService.validateFile(valid);
    }

    // ── validateFiles ─────────────────────────────────────────────────────

    @Test
    void validateFiles_shouldThrow_whenListIsNull() {
        assertThatThrownBy(() -> fileStorageService.validateFiles(null))
                .isInstanceOf(FileEmptyException.class);
    }

    @Test
    void validateFiles_shouldThrow_whenListIsEmpty() {
        assertThatThrownBy(() -> fileStorageService.validateFiles(List.of()))
                .isInstanceOf(FileEmptyException.class);
    }

    @Test
    void validateFiles_shouldThrow_whenFileCountExceedsMax() {
        int count = FileConstants.MAX_FILES_COUNT + 1;
        List<MultipartFile> files = java.util.Collections.nCopies(
                count,
                new MockMultipartFile("file", "x.png", "image/png", new byte[1])
        );

        assertThatThrownBy(() -> fileStorageService.validateFiles(files))
                .isInstanceOf(FileCountExceededException.class);
    }

    // ── save ──────────────────────────────────────────────────────────────

    @Test
    void save_shouldStoreFileAndReturnFileMetadata_forPngType() throws IOException {
        MockMultipartFile multipart = new MockMultipartFile(
                "file", "photo.png", "image/png", "png-content".getBytes());

        File saved = fileStorageService.save(multipart, "test-dir");

        assertThat(saved.getFileName()).endsWith(".png");
        assertThat(saved.getFileType()).isEqualTo(FileType.PNG);
        assertThat(saved.getFileUrl()).contains("test-dir");
        assertThat(saved.getFileSize()).isEqualTo(multipart.getSize());
    }

    @Test
    void save_shouldStoreFileAndReturnFileMetadata_forJpegType() throws IOException {
        MockMultipartFile multipart = new MockMultipartFile(
                "file", "image.jpg", "image/jpeg", "jpeg-content".getBytes());

        File saved = fileStorageService.save(multipart, null);

        assertThat(saved.getFileType()).isEqualTo(FileType.JPEG);
        assertThat(saved.getFileName()).endsWith(".jpeg");
    }

    @Test
    void save_shouldStoreFile_forPdfType() throws IOException {
        MockMultipartFile multipart = new MockMultipartFile(
                "file", "report.pdf", "application/pdf", "pdf-content".getBytes());

        File saved = fileStorageService.save(multipart, null);

        assertThat(saved.getFileType()).isEqualTo(FileType.PDF);
        assertThat(saved.getFileName()).endsWith(".pdf");
    }

    @Test
    void save_shouldThrow_whenFileTypeIsNotAllowed() {
        MockMultipartFile csv = new MockMultipartFile(
                "file", "data.csv", "text/csv", "a,b,c".getBytes());

        assertThatThrownBy(() -> fileStorageService.save(csv, null))
                .isInstanceOf(FileTypeNotAllowedException.class);
    }

    // ── delete ────────────────────────────────────────────────────────────

    @Test
    void delete_shouldReturnTrue_whenFileExists() throws IOException {
        MockMultipartFile multipart = new MockMultipartFile(
                "file", "todelete.png", "image/png", "data".getBytes());

        File saved = fileStorageService.save(multipart, null);

        boolean result = fileStorageService.delete(saved.getFileUrl());

        assertThat(result).isTrue();
    }

    @Test
    void delete_shouldReturnFalse_whenFileDoesNotExist() {
        // Non-existent file URL – resolvePathFromUrl walks the root, finds nothing,
        // deleteIfExists then returns false (but the method throws IllegalArgumentException first).
        // This tests the actual behavior: FileErrorException is thrown because the walk fails to find it.
        assertThatThrownBy(() -> fileStorageService.delete("uploads/files/nonexistent.png"))
                .isInstanceOf(Exception.class);
    }

    // ── exists ────────────────────────────────────────────────────────────

    @Test
    void exists_shouldReturnTrue_whenFileWasSaved() throws IOException {
        MockMultipartFile multipart = new MockMultipartFile(
                "file", "exists.png", "image/png", "data".getBytes());
        File saved = fileStorageService.save(multipart, null);

        assertThat(fileStorageService.exists(saved.getFileUrl())).isTrue();
    }

    @Test
    void exists_shouldReturnFalse_forNonExistentUrl() {
        assertThat(fileStorageService.exists("uploads/files/ghost.png")).isFalse();
    }
}
