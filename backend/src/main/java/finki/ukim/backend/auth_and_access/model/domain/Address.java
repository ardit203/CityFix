package finki.ukim.backend.auth_and_access.model.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String street;
    private String city;
    private String postalCode;
}
