package finki.ukim.backend.notification.model.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.common.model.BaseAuditableEntity;
import finki.ukim.backend.notification.model.enums.NotificationStatus;
import finki.ukim.backend.notification.model.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 3000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status = NotificationStatus.PENDING;

    private LocalDateTime sentAt;

    private String providerMessageId;

    private String failureReason;

    private Integer retryCount = 0;

    private LocalDateTime lastTriedAt;

    public Notification(User recipient, NotificationType type, String subject, String message) {
        this.recipient = recipient;
        this.type = type;
        this.subject = subject;
        this.message = message;
    }
}