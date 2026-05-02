package finki.ukim.backend.request_management.model.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.common.model.BaseEntity;
import finki.ukim.backend.request_management.model.enums.LogAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "action_by_user_id", nullable = false)
    private User actionBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogAction action;

    private String oldValue;
    private String newValue;
    private String note;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public RequestLog(
            Request request,
            User actionBy,
            LogAction action,
            String oldValue,
            String newValue,
            String note
    ) {
        this.request = request;
        this.actionBy = actionBy;
        this.action = action;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.note = note;
        this.createdAt = LocalDateTime.now();
    }
}
