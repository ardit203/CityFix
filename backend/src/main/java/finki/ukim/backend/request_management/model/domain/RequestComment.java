package finki.ukim.backend.request_management.model.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.common.model.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "request_comments")
@Getter
@Setter
@NoArgsConstructor
public class RequestComment extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false)
    private Boolean isInternal = false;

    public RequestComment(Request request, User author, String content, Boolean isInternal) {
        this.request = request;
        this.author = author;
        this.content = content;
        this.isInternal = isInternal != null ? isInternal : false;
    }
}
