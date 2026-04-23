package finki.ukim.backend.service.file_handling;

import finki.ukim.backend.file_handling.model.domain.File;
import finki.ukim.backend.file_handling.model.enums.FileType;
import finki.ukim.backend.file_handling.repository.FileRepository;
import finki.ukim.backend.file_handling.service.domain.FileStorageService;
import finki.ukim.backend.file_handling.service.domain.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock private FileRepository    fileRepository;
    @Mock private FileStorageService fileStorageService;

    @InjectMocks
    private FileServiceImpl fileService;

    private File pngFile;
    private MockMultipartFile multipart;

    @BeforeEach
    void setUp() {
        pngFile     = new File("photo.png", FileType.PNG, "uploads/files/photo.png", 1024L);
        multipart   = new MockMultipartFile("file", "photo.png", "image/png", "data".getBytes());
    }

    // ── create ────────────────────────────────────────────────────────────

    @Test
    void create_withDirectory_shouldSaveAndReturnFile() {
        when(fileStorageService.save(multipart, "profile-pics")).thenReturn(pngFile);
        when(fileRepository.save(pngFile)).thenReturn(pngFile);

        File result = fileService.create(multipart, "profile-pics");

        assertThat(result).isEqualTo(pngFile);
        verify(fileStorageService).save(multipart, "profile-pics");
        verify(fileRepository).save(pngFile);
    }

    @Test
    void create_withoutDirectory_shouldUseNullDirectory() {
        when(fileStorageService.save(multipart, null)).thenReturn(pngFile);
        when(fileRepository.save(pngFile)).thenReturn(pngFile);

        File result = fileService.create(multipart);

        assertThat(result).isEqualTo(pngFile);
        verify(fileStorageService).save(multipart, null);
    }

    // ── createAll ─────────────────────────────────────────────────────────

    @Test
    void createAll_shouldSaveAllFilesAndReturnList() {
        File pdfFile = new File("doc.pdf", FileType.PDF, "uploads/files/doc.pdf", 2048L);
        MockMultipartFile multipart2 = new MockMultipartFile("f2", "doc.pdf", "application/pdf", "pdf".getBytes());
        List<MultipartFile> inputs = List.of(multipart, multipart2);

        when(fileStorageService.saveAll(anyList(), isNull())).thenReturn(List.of(pngFile, pdfFile));
        when(fileRepository.saveAll(anyList())).thenReturn(List.of(pngFile, pdfFile));

        List<File> results = fileService.createAll(inputs);

        assertThat(results).hasSize(2);
    }

    // ── update ────────────────────────────────────────────────────────────

    @Test
    void update_shouldReplaceOldFileAndSave_whenIdExists() {
        File newStorageFile = new File("new.png", FileType.PNG, "uploads/files/new.png", 512L);

        when(fileRepository.findById(1L)).thenReturn(Optional.of(pngFile));
        when(fileStorageService.save(multipart, null)).thenReturn(newStorageFile);
        when(fileRepository.save(any(File.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<File> result = fileService.update(1L, multipart);

        assertThat(result).isPresent();
        assertThat(result.get().getFileName()).isEqualTo("new.png");
        assertThat(result.get().getFileUrl()).isEqualTo("uploads/files/new.png");
        verify(fileStorageService).delete("uploads/files/photo.png");
    }

    @Test
    void update_shouldReturnEmpty_whenFileNotFound() {
        when(fileRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<File> result = fileService.update(99L, multipart);

        assertThat(result).isEmpty();
        verify(fileStorageService, never()).save(any(), any());
        verify(fileStorageService, never()).delete(any());
    }

    // ── deleteById ────────────────────────────────────────────────────────

    @Test
    void deleteById_shouldDeleteFromStorageAndRepo_whenExists() {
        when(fileRepository.findById(1L)).thenReturn(Optional.of(pngFile));

        Optional<File> result = fileService.deleteById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(pngFile);
        verify(fileStorageService).delete("uploads/files/photo.png");
        verify(fileRepository).delete(pngFile);
    }

    @Test
    void deleteById_shouldReturnEmpty_whenFileNotFound() {
        when(fileRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<File> result = fileService.deleteById(99L);

        assertThat(result).isEmpty();
        verify(fileStorageService, never()).delete(any());
        verify(fileRepository, never()).delete(any());
    }

    // ── findByFileType ────────────────────────────────────────────────────

    @Test
    void findByFileType_shouldReturnMatchingFiles() {
        when(fileRepository.findByFileType(FileType.PNG)).thenReturn(List.of(pngFile));

        List<File> results = fileService.findByFileType(FileType.PNG);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFileType()).isEqualTo(FileType.PNG);
    }

    @Test
    void findByFileType_shouldReturnEmpty_whenNoFilesMatch() {
        when(fileRepository.findByFileType(FileType.CSV)).thenReturn(List.of());

        List<File> results = fileService.findByFileType(FileType.CSV);

        assertThat(results).isEmpty();
    }
}
