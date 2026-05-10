package finki.ukim.backend.request_management.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.domain.RequestComment;

import java.util.List;

public interface RequestCommentService {

    List<RequestComment> findAllByRequest(Long requestId, User currentUser);

    RequestComment create(Long requestId, String content, Boolean internal, User currentUser);

    RequestComment update(Long commentId, String content, Boolean internal, User currentUser);

    RequestComment delete(Long commentId, User currentUser);
}
