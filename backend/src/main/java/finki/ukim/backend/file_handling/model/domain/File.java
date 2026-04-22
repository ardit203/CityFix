package finki.ukim.backend.file_handling.model.domain;

import finki.ukim.backend.common.model.BaseAuditableEntity;
import finki.ukim.backend.file_handling.model.enums.FileType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "files")
public class File extends BaseAuditableEntity {
    @Column(nullable = false, unique = true)
    private String fileName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Column(nullable = false, unique = true)
    private String fileUrl;

    @Max(value = 10, message = "File size must be less than or equal to 10 MB")
    @Column(nullable = false)
    private Long fileSize;

    public File(String fileName, FileType fileType, Long fileSize) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }
}
