package finki.ukim.backend.auth_and_access.model.domain;

import finki.ukim.backend.common.model.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "password_reset_tokens")
@NamedEntityGraph(
        name = "password-reset-token-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("user")
        }
)
public class PasswordResetToken extends BaseAuditableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String tokenHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime usedAt;
    private LocalDateTime invalidatedAt;

    public PasswordResetToken(User user, String token, LocalDateTime expiresAt) {
        this.user = user;
        this.token = token;
        this.expiresAt = expiresAt;
    }


    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }

    public boolean isUsed() {
        return usedAt != null;
    }

    public boolean isInvalidated() {
        return invalidatedAt != null;
    }

    public boolean isActive() {
        return !isExpired() && !isUsed() && !isInvalidated();
    }

    public void markAsUsed() {
        this.usedAt = LocalDateTime.now();
    }

    public void invalidate() {
        this.invalidatedAt = LocalDateTime.now();
    }
}
