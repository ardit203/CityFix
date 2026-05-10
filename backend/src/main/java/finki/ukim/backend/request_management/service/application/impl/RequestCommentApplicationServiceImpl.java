package finki.ukim.backend.request_management.service.application.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.domain.RequestComment;
import finki.ukim.backend.request_management.model.dto.CreateRequestCommentDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestCommentDto;
import finki.ukim.backend.request_management.model.dto.UpdateRequestCommentDto;
import finki.ukim.backend.request_management.service.application.RequestCommentApplicationService;
import finki.ukim.backend.request_management.service.domain.RequestCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestCommentApplicationServiceImpl implements RequestCommentApplicationService {

    private final RequestCommentService requestCommentService;

    @Override
    public List<DisplayRequestCommentDto> findAllByRequest(Long requestId, User currentUser) {
        return requestCommentService.findAllByRequest(requestId, currentUser)
                .stream()
                .map(DisplayRequestCommentDto::from)
                .toList();
    }

    @Override
    public DisplayRequestCommentDto create(Long requestId, CreateRequestCommentDto dto, User currentUser) {
        RequestComment comment = requestCommentService.create(
                requestId,
                dto.content(),
                dto.internal(),
                currentUser
        );
        return DisplayRequestCommentDto.from(comment);
    }

    @Override
    public DisplayRequestCommentDto update(Long requestId, Long commentId, UpdateRequestCommentDto dto, User currentUser) {
        // Note: requestId could be used for extra validation if needed, but the service handles it via commentId
        RequestComment comment = requestCommentService.update(
                commentId,
                dto.content(),
                dto.internal(),
                currentUser
        );
        return DisplayRequestCommentDto.from(comment);
    }

    @Override
    public DisplayRequestCommentDto delete(Long requestId, Long commentId, User currentUser) {
        RequestComment comment = requestCommentService.delete(commentId, currentUser);
        return DisplayRequestCommentDto.from(comment);
    }
}
