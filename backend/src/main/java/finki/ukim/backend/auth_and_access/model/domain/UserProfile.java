package finki.ukim.backend.auth_and_access.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_profiles")
@NamedEntityGraph(
        name = "user-profile-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("user")
        }
)
public class UserProfile {
    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Embedded
    private Address address;

    private String profilePictureUrl;


    public UserProfile(User user, String name, String surname, Address address) {
        this.user = user;
        this.name = name;
        this.surname = surname;
        this.address = address;
    }

    public UserProfile(User user, String name, String surname, String street, String city, String postalCode) {
        this.user = user;
        this.name = name;
        this.surname = surname;
        this.address = new Address(street, city, postalCode);
    }
}
