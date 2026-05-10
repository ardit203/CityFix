package finki.ukim.backend.request_management.model.projection;

public interface RequestFileProjection {
    Long getId();

    Long getFileId();

    Long getRequestId();

    String getFileName();

    String getFileUrl();
}
