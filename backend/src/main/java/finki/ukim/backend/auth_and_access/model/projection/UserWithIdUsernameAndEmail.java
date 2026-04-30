package finki.ukim.backend.auth_and_access.model.projection;

public interface UserWithIdUsernameAndEmail {
    Long getId();

    String getUsername();

    String getEmail();
}
