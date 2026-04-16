package finki.ukim.backend.auth_and_access.model.domain;

import finki.ukim.backend.auth_and_access.model.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    private Long userId;

    @MapsId
    @OneToOne(optional = false)
    private User user;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String surname;

    private String phoneNumber;

    @Embedded
    private Address address;

    private LocalDate dateOfBirth;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String profilePictureUrl;

    public UserProfile(User user, String name, String surname, Address address, LocalDate dateOfBirth, Gender gender, String phoneNumber) {
        this.user = user;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public UserProfile(String name, String surname, Address address, LocalDate dateOfBirth, Gender gender, String phoneNumber){
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
}
