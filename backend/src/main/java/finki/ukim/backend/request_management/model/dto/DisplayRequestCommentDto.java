package finki.ukim.backend.request_management.model.dto;

import finki.ukim.backend.request_management.model.domain.RequestComment;

import java.time.LocalDateTime;

public record DisplayRequestCommentDto(
        Long id,
        Long requestId,
        Long authorId,
        String authorUsername,
        String content,
        Boolean internal,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static DisplayRequestCommentDto from(RequestComment comment) {
        return new DisplayRequestCommentDto(
                comment.getId(),
                comment.getRequest().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getUsername(),
                comment.getContent(),
                comment.getIsInternal(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}