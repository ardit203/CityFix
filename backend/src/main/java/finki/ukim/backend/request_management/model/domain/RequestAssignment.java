package finki.ukim.backend.request_management.model.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestAssignment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_user_id", nullable = false)
    private User employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assigned_by_user_id", nullable = false)
    private User assignedBy;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    public RequestAssignment(
            Request request,
            User employee,
            User assignedBy
    ) {
        this.request = request;
        this.employee = employee;
        this.assignedBy = assignedBy;
        this.assignedAt = LocalDateTime.now();
    }
}