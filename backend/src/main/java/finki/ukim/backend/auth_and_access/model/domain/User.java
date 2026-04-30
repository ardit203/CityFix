package finki.ukim.backend.auth_and_access.model.domain;

import finki.ukim.backend.auth_and_access.model.enums.Gender;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.common.model.BaseAuditableEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseAuditableEntity implements UserDetails {
    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile profile;

    @Column(nullable = false)
    private Integer failedLoginAttempts = 0;

    private LocalDateTime lockedUntil;

    private Boolean notificationsEnabled = true;

    private Integer lockLevel = 0;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String username, String password, String email, Role role, Boolean notificationsEnabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.notificationsEnabled = notificationsEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }


    public Boolean isLocked() {
        return lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now());
    }

    public void setProfile(UserProfile userProfile) {
        this.profile = userProfile;
        profile.setUser(this);
    }
}
