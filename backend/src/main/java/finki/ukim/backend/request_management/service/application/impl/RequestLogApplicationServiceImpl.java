package finki.ukim.backend.request_management.service.application.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.DisplayRequestLogBasicDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestLogDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestLogPageableDto;
import finki.ukim.backend.request_management.model.dto.filter.RequestLogFilterDto;
import finki.ukim.backend.request_management.service.application.RequestLogApplicationService;
import finki.ukim.backend.request_management.service.domain.RequestLogService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestLogApplicationServiceImpl implements RequestLogApplicationService {
    private final RequestLogService requestLogService;

    @Override
    public DisplayRequestLogDto findById(Long id, User user) {
        return DisplayRequestLogDto.from(requestLogService.findById(id, user));
    }

    @Override
    public List<DisplayRequestLogBasicDto> findAllByRequestId(Long requestId, User user) {
        return DisplayRequestLogBasicDto.from(
                requestLogService.findAllByRequestId(requestId, user)
        );
    }

    @Override
    public Page<DisplayRequestLogPageableDto> findAll(Long requestId, User user, RequestLogFilterDto requestLogFilterDto) {
        return requestLogService.findAll(requestId, user, requestLogFilterDto)
                .map(DisplayRequestLogPageableDto::from);
    }
}
