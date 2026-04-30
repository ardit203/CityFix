package finki.ukim.backend.file_handling.helper;

import finki.ukim.backend.file_handling.constants.FileConstants;
import finki.ukim.backend.file_handling.model.enums.FileType;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Component
public class FileHelper {
    public String generateFileName(FileType fileType, String originalFilename) {
        String extension = getExtension(fileType);

        String safeBaseName = FileConstants.BASE_NAME;
        if (originalFilename != null && !originalFilename.isBlank()) {
            safeBaseName = Paths.get(originalFilename).getFileName().toString();

            int dotIndex = safeBaseName.lastIndexOf('.');
            if (dotIndex > 0) {
                safeBaseName = safeBaseName.substring(0, dotIndex);
            }

            safeBaseName = safeBaseName.replaceAll("[^a-zA-Z0-9-_]", "_");
            if (safeBaseName.isBlank()) {
                safeBaseName = FileConstants.BASE_NAME;
            }
        }

        return UUID.randomUUID() + "_" + safeBaseName + extension;
    }

    public String getExtension(FileType fileType) {
        if (fileType == null || fileType == FileType.OTHER) return null;
        return "." + fileType.name().toLowerCase();
    }

    public FileType getFileType(String fileType) {
        if (fileType == null) {
            return FileType.OTHER;
        }

        return switch (fileType) {
            case "image/png" -> FileType.PNG;
            case "image/jpeg" -> FileType.JPEG;
            case "application/pdf" -> FileType.PDF;
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> FileType.XLSX;
            case "application/vnd.ms-excel" -> FileType.XLS;
            case "text/csv" -> FileType.CSV;
            default -> FileType.OTHER;
        };
    }

    public Boolean isImage(MultipartFile file) {
        List<FileType> fileTypeList = getImageTypes();

        if (file == null) {
            return false;
        }
        FileType fileType = getFileType(file.getContentType());
        return fileTypeList.contains(fileType);
    }

    private List<FileType> getImageTypes() {
        return List.of(FileType.PNG, FileType.JPEG);
    }
}
