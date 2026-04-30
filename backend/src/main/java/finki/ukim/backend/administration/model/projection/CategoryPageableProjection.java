package finki.ukim.backend.administration.model.projection;

public interface CategoryPageableProjection {
    Long getId();

    String getName();

    Long getDepartmentId();

    String getDepartmentName();
}
