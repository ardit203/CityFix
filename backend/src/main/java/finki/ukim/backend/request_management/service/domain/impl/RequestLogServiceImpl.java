package finki.ukim.backend.request_management.service.domain.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.service.domain.AccessScopeService;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.domain.RequestLog;
import finki.ukim.backend.request_management.model.dto.filter.RequestLogFilterDto;
import finki.ukim.backend.request_management.model.enums.LogAction;
import finki.ukim.backend.request_management.model.exception.RequestLogNotFoundException;
import finki.ukim.backend.request_management.model.exception.RequestNotFoundException;
import finki.ukim.backend.request_management.model.projection.RequestLogPageableProjection;
import finki.ukim.backend.request_management.repository.RequestLogRepository;
import finki.ukim.backend.request_management.repository.RequestRepository;
import finki.ukim.backend.request_management.service.domain.RequestLogService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestLogServiceImpl implements RequestLogService {
    private final RequestLogRepository requestLogRepository;
    private final AccessScopeService accessScopeService;
    private final RequestRepository requestRepository;

    @Override
    public RequestLog create(
            Request request,
            User actionBy,
            LogAction action,
            String oldValue,
            String newValue,
            String note
    ) {
        return requestLogRepository.save(new RequestLog(request, actionBy, action, oldValue, newValue, note));
    }

    @Override
    public RequestLog findById(Long id, User user) {
        RequestLog log = requestLogRepository
                .findByIdWithRequest(id)
                .orElseThrow(() -> new RequestLogNotFoundException(id));

        accessScopeService.hasAccessToRequest(user, log.getRequest());
        return log;
    }

    @Override
    public List<RequestLog> findAllByRequestId(Long requestId, User user) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
        accessScopeService.hasAccessToRequest(user, request);
        return requestLogRepository.findAllByRequestId(requestId);
    }

    @Override
    public Page<RequestLogPageableProjection> findAll(Long requestId, User user, RequestLogFilterDto requestLogFilterDto) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));

        accessScopeService.hasAccessToRequest(user, request);
        if (accessScopeService.isCitizen(user) || accessScopeService.isEmployee(user)) {
            requestLogFilterDto.setActionByUserId(null);
        }

        requestLogFilterDto.normalizeTextFields();

        return requestLogRepository.findFiltered(
                requestLogFilterDto.getId(),
                requestId,
                requestLogFilterDto.getActionByUserId(),
                requestLogFilterDto.getAction(),
                requestLogFilterDto.getText(),
                requestLogFilterDto.getCreatedFrom(),
                requestLogFilterDto.getCreatedTo(),
                requestLogFilterDto.toPageable()
        );
    }
}
