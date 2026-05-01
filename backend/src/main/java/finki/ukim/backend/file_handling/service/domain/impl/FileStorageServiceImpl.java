package finki.ukim.backend.file_handling.service.domain.impl;

import finki.ukim.backend.file_handling.constants.FileConstants;
import finki.ukim.backend.file_handling.helper.FileHelper;
import finki.ukim.backend.file_handling.model.domain.File;
import finki.ukim.backend.file_handling.model.enums.FileType;
import finki.ukim.backend.file_handling.model.exception.*;
import finki.ukim.backend.file_handling.service.domain.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private final FileHelper fileHelper;

    @Override
    public File save(MultipartFile file, String directory) {
        validateFile(file);

        directory = normalizeDirectory(directory);

        FileType fileType = fileHelper.getFileType(file.getContentType());

        if (!FileConstants.ALLOWED_FILE_TYPES.contains(fileType)) {
            throw new FileTypeNotAllowedException(fileType);
        }

        Path dir = FileConstants.ROOT.resolve(directory).normalize();
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new FileErrorException("Failed to create directory: " + directory);
        }


        String fileName = fileHelper.generateFileName(fileType, file.getOriginalFilename());
        Path target = dir.resolve(fileName).normalize();

        if (!target.startsWith(dir)) {
            throw new FileErrorException("Invalid file path: " + fileName);
        }

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileErrorException("Failed to save file: " + fileName);
        }

        return new File(
                fileName,
                fileType,
                FileConstants.getBaseUrl(directory) + fileName,
                file.getSize()
        );
    }

    @Override
    public List<File> saveAll(List<MultipartFile> files, String directory) {
        validateFiles(files);

        List<File> savedFiles = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                savedFiles.add(save(file, directory));
            }
            return savedFiles;
        } catch (RuntimeException e) {
            for (File savedFile : savedFiles) {
                try {
                    delete(savedFile.getFileUrl());
                } catch (Exception ignored) {
                }
            }
            throw e;
        }
    }

    @Override
    public boolean delete(String fileUrl) {
        try {
            Path path = resolvePathFromUrl(fileUrl);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileErrorException("Failed to delete file: " + fileUrl);
        }
    }

    @Override
    public Path load(String fileUrl) {
        Path path = resolvePathFromUrl(fileUrl);

        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new FileNotFoundException(fileUrl);
        }

        return path;
    }

    @Override
    public boolean exists(String fileUrl) {
        try {
            Path path = resolvePathFromUrl(fileUrl);
            return Files.exists(path) && Files.isRegularFile(path);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileEmptyException();
        }

        if (file.getSize() > FileConstants.MAX_FILE_SIZE) {
            throw new FileSizeExceededException();
        }
    }

    @Override
    public void validateFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new FileEmptyException();
        }

        if (files.size() > FileConstants.MAX_FILES_COUNT) {
            throw new FileCountExceededException(files.size());
        }

        for (MultipartFile file : files) {
            validateFile(file);
        }

        long totalSize = files.stream()
                .mapToLong(MultipartFile::getSize)
                .sum();

        if (totalSize > FileConstants.MAX_TOTAL_SIZE) {
            throw new FileSizeExceededException(files.size());
        }
    }

    private String normalizeDirectory(String directory) {
        if (directory == null || directory.isBlank()) {
            return FileConstants.DEFAULT_DIR;
        }
        return directory.trim();
    }

    private Path resolvePathFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            throw new FileErrorException("File URL cannot be null or blank");
        }

        String normalizedUrl = fileUrl.trim().replace("\\", "/");
        String defaultUrl = FileConstants.DEFAULT_URL.replace("\\", "/");
        
        String relativePath;
        if (normalizedUrl.startsWith(defaultUrl)) {
            relativePath = normalizedUrl.substring(defaultUrl.length());
            // Remove leading slashes that might be present due to how getBaseUrl is constructed
            while (relativePath.startsWith("/")) {
                relativePath = relativePath.substring(1);
            }
        } else {
            relativePath = extractFileName(normalizedUrl);
        }

        Path exactPath = FileConstants.ROOT.resolve(relativePath).normalize();
        
        if (Files.exists(exactPath) && Files.isRegularFile(exactPath)) {
            return exactPath;
        }

        // Fallback for any legacy URLs that don't match the new deterministic path calculation
        String fileName = extractFileName(fileUrl);
        try (Stream<Path> stream = Files.walk(FileConstants.ROOT)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().equals(fileName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("File not found: " + fileUrl));
        } catch (IOException e) {
            throw new FileErrorException("Failed to resolve file path: " + fileUrl);
        }
    }

    private String extractFileName(String fileUrl) {
        String value = fileUrl.trim().replace("\\", "/");

        int lastSlash = value.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < value.length() - 1) {
            return value.substring(lastSlash + 1);
        }

        return value;
    }
}
