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

    public static String PROFILE_PIC_DIR = "profile";
    public static String REQUESTS_DIR = "requests";

    public static Long getMaxFileSizeInMB() {
        return MAX_FILE_SIZE / (1024 * 1024);
    }

    public static String getBaseUrl(String directory) {
        return DEFAULT_URL + "/" + directory + "/";
    }
}
