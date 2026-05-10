package finki.ukim.backend.request_management.service.application;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.dto.DisplayRequestLogBasicDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestLogDto;
import finki.ukim.backend.request_management.model.dto.DisplayRequestLogPageableDto;
import finki.ukim.backend.request_management.model.dto.filter.RequestLogFilterDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RequestLogApplicationService {
    DisplayRequestLogDto findById(Long id, User user);

    List<DisplayRequestLogBasicDto> findAllByRequestId(Long requestId, User user);

    Page<DisplayRequestLogPageableDto> findAll(Long requestId, User user, RequestLogFilterDto requestLogFilterDto);
}
