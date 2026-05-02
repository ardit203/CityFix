package finki.ukim.backend.request_management.model.domain;

import finki.ukim.backend.common.model.BaseAuditableEntity;
import finki.ukim.backend.file_handling.model.domain.File;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "request_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestFile extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    public RequestFile(Request request, File file) {
        this.request = request;
        this.file = file;
    }
}