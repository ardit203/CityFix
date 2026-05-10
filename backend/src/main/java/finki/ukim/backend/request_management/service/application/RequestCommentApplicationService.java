package finki.ukim.backend.request_management.service.application;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.CreateRequestCommentDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestCommentDto;
import finki.ukim.backend.request_management.model.dto.UpdateRequestCommentDto;

import java.util.List;

public interface RequestCommentApplicationService {

    List<DisplayRequestCommentDto> findAllByRequest(Long requestId, User currentUser);

    DisplayRequestCommentDto create(Long requestId, CreateRequestCommentDto dto, User currentUser);

    DisplayRequestCommentDto update(Long requestId, Long commentId, UpdateRequestCommentDto dto, User currentUser);

    DisplayRequestCommentDto delete(Long requestId, Long commentId, User currentUser);
}
