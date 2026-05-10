package finki.ukim.backend.request_management.service.domain;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.domain.RequestLog;
import finki.ukim.backend.request_management.model.dto.filter.RequestLogFilterDto;
import finki.ukim.backend.request_management.model.enums.LogAction;
import finki.ukim.backend.request_management.model.projection.RequestLogPageableProjection;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RequestLogService {
    RequestLog create(
            Request request,
            User actionBy,
            LogAction action,
            String oldValue,
            String newValue,
            String note
    );

    RequestLog findById(Long id, User user);

    List<RequestLog> findAllByRequestId(Long requestId, User user);

    Page<RequestLogPageableProjection> findAll(Long requestId, User user, RequestLogFilterDto requestLogFilterDto);
}
