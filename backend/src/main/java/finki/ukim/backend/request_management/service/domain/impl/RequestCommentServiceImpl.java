package finki.ukim.backend.request_management.service.domain.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.service.domain.AccessScopeService;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.domain.RequestComment;
import finki.ukim.backend.request_management.model.enums.LogAction;
import finki.ukim.backend.request_management.model.exception.RequestCommentNotFoundException;
import finki.ukim.backend.request_management.repository.RequestCommentRepository;
import finki.ukim.backend.request_management.service.domain.RequestCommentService;
import finki.ukim.backend.request_management.service.domain.RequestLogService;
import finki.ukim.backend.request_management.service.domain.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestCommentServiceImpl implements RequestCommentService {
    private final RequestCommentRepository requestCommentRepository;
    private final RequestService requestService;
    private final AccessScopeService accessScopeService;
    private final RequestLogService requestLogService;

    @Override
    public List<RequestComment> findAllByRequest(Long requestId, User currentUser) {
        Request request = requestService.findById(requestId, currentUser);

        accessScopeService.hasAccessToRequest(currentUser,request);

        if (accessScopeService.isCitizen(currentUser)) {
            return requestCommentRepository.findAllByRequestIdAndIsInternalFalse(requestId);
        } else {
            return requestCommentRepository.findAllByRequestId(requestId);
        }
    }

    @Override
    public RequestComment create(Long requestId, String content, Boolean internal, User currentUser) {
        Request request = requestService.findById(requestId, currentUser);

        Boolean isInternal = internal;
        if (accessScopeService.isCitizen(currentUser)) {
            isInternal = false;
        }

        RequestComment comment = new RequestComment(request, currentUser, content, isInternal);
        RequestComment savedComment = requestCommentRepository.save(comment);
        requestLogService.create(
                request,
                currentUser,
                LogAction.COMMENT_ADDED,
                null,
                savedComment.getId() + " - " + visibilityLabel(savedComment.getIsInternal()),
                savedComment.getIsInternal()
                        ? "Internal comment added."
                        : "Public comment added."
        );


        return savedComment;
    }

    @Override
    public RequestComment update(Long commentId, String content, Boolean internal, User currentUser) {
        RequestComment comment = requestCommentRepository.findById(commentId)
                .orElseThrow(() -> new RequestCommentNotFoundException(commentId));

        // Only the author or an admin can edit a comment
        if (!comment.getAuthor().getId().equals(currentUser.getId()) && !accessScopeService.isAdmin(currentUser)) {
            throw new IllegalArgumentException("You do not have permission to edit this comment.");
        }

        String oldValue = formatCommentForLog(comment);

        comment.setContent(content);

        if (!accessScopeService.isCitizen(currentUser)) {
            comment.setIsInternal(internal != null ? internal : false);
        }

        RequestComment savedComment = requestCommentRepository.save(comment);

        requestLogService.create(
                savedComment.getRequest(),
                currentUser,
                LogAction.COMMENT_EDITED,
                oldValue,
                formatCommentForLog(savedComment),
                "Comment updated."
        );

        return savedComment;
    }

    @Override
    public RequestComment delete(Long commentId, User currentUser) {
        RequestComment comment = requestCommentRepository.findById(commentId)
                .orElseThrow(() -> new RequestCommentNotFoundException(commentId));

        // Only the author or an admin can delete a comment
        if (!comment.getAuthor().getId().equals(currentUser.getId()) && !accessScopeService.isAdmin(currentUser)) {
            throw new IllegalArgumentException("You do not have permission to delete this comment.");
        }

        Request request = comment.getRequest();

        requestLogService.create(
                request,
                currentUser,
                LogAction.COMMENT_DELETED,
                formatCommentForLog(comment),
                null,
                "Comment deleted."
        );

        requestCommentRepository.delete(comment);

        return comment;
    }


    private String formatCommentForLog(RequestComment comment) {
        return comment.getId() + " - " + visibilityLabel(comment.getIsInternal());
    }

    private String visibilityLabel(Boolean internal) {
        return Boolean.TRUE.equals(internal) ? "Internal comment" : "Public comment";
    }
}
