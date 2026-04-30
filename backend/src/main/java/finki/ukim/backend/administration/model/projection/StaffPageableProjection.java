package finki.ukim.backend.administration.model.projection;

public interface StaffPageableProjection {
    Long getId();

    String getName();

    String getSurname();

    String getEmail();

    String getUsername();

    String getDepartmentName();

    String getMunicipalityName();

    String getMunicipalityCode();
}
