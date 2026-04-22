package finki.ukim.backend.file_handling.constants;

import finki.ukim.backend.file_handling.model.enums.FileType;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Configuration
public class FileConstants {
    public static String UPLOAD_DIR = "uploads";
    public static String DEFAULT_DIR = "files";
    public static Path ROOT = Path.of(UPLOAD_DIR);
    public static String DEFAULT_URL = UPLOAD_DIR + "/" + DEFAULT_DIR + "/";
    public static Long MAX_FILE_SIZE = 10L * 1024 * 1024; // 10 MB
    public static Integer MAX_FILES_COUNT = 5;
    public static String BASE_NAME = "file";
    public static Long MAX_TOTAL_SIZE = 10L * 1024 * 1024; // 50 MB
    public static List<FileType> ALLOWED_FILE_TYPES = List.of(
            FileType.PNG,
            FileType.JPEG,
            FileType.PDF
    );


    public static void setMaxFilesCount(Integer maxFilesCount) {
        MAX_FILES_COUNT = maxFilesCount;
    }

    public static void setMaxFileSize(Long maxFileSize) {
        MAX_FILE_SIZE = maxFileSize;
    }

    public static Long getMaxFileSizeInMB() {
        return MAX_FILE_SIZE / (1024 * 1024);
    }

    public static Long getMaxTotalSizeInMB() {
        return MAX_TOTAL_SIZE / (1024 * 1024);
    }

    public static void setMaxTotalSize(Long maxTotalSize) {
        MAX_TOTAL_SIZE = maxTotalSize;
    }


    public static void setUploadDir(String uploadDir) {
        UPLOAD_DIR = uploadDir;
        setROOT(uploadDir);
        setDefaultUrl(uploadDir + "/" + DEFAULT_DIR + "/");
    }

    public static void setDefaultDir(String defaultDir) {
        DEFAULT_DIR = defaultDir;
        setDefaultUrl(UPLOAD_DIR + "/" + DEFAULT_DIR + "/");
    }

    public static void setROOT(String path) {
        FileConstants.ROOT = Path.of(path);
    }

    public static void setDefaultUrl(String defaultUrl) {
        DEFAULT_URL = defaultUrl;
    }

    public static String getBaseUrl(String directory) {
        return UPLOAD_DIR + "/" + directory + "/";
    }
}
