package finki.ukim.backend.ai_integration.model.domain;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.ai_integration.model.enums.SuggestionStatus;
import finki.ukim.backend.common.model.BaseAuditableEntity;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.enums.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ai_suggestions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiSuggestion extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(length = 2000)
    private String aiSummary;

    @Enumerated(EnumType.STRING)
    private SuggestionStatus suggestionStatus = SuggestionStatus.PENDING_REVIEW;

}
