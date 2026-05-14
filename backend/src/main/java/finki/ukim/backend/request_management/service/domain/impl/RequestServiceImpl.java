package finki.ukim.backend.request_management.service.domain.impl;

import finki.ukim.backend.ai_integration.service.domain.AiSuggestionService;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.RequestScopeFilters;
import finki.ukim.backend.auth_and_access.service.domain.AccessScopeService;
import finki.ukim.backend.file_handling.constants.FileConstants;
import finki.ukim.backend.file_handling.model.domain.File;
import finki.ukim.backend.file_handling.service.domain.FileService;
import finki.ukim.backend.notification.model.events.RequestCommentAdded;
import finki.ukim.backend.notification.model.events.RequestCreatedEvent;
import finki.ukim.backend.notification.model.events.RequestStatusChangedEvent;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.dto.ChangeRequestStatusDto;
import finki.ukim.backend.request_management.model.dto.filter.RequestFilterDto;
import finki.ukim.backend.request_management.model.enums.LogAction;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import finki.ukim.backend.request_management.model.exception.InvalidRequestStatusTransitionException;
import finki.ukim.backend.request_management.model.exception.RequestCannotBeCanceled;
import finki.ukim.backend.request_management.model.exception.RequestNotFoundException;
import finki.ukim.backend.request_management.model.projection.RequestPageableProjection;
import finki.ukim.backend.request_management.repository.RequestRepository;
import finki.ukim.backend.request_management.service.domain.RequestLogService;
import finki.ukim.backend.request_management.service.domain.RequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final AccessScopeService accessScopeService;
    private final RequestLogService requestLogService;
    private final AiSuggestionService aiSuggestionService;
    private final FileService fileService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public List<Request> findAll(User user) {
        return requestRepository.findAll();
    }

    @Override
    public Request findById(Long id, User user) {
        Request request = requestRepository
                .findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
        accessScopeService.hasAccessToRequest(user, request);
        return request;
    }

    @Override
    public Request create(Request request, List<MultipartFile> files) {
        List<File> createdFiles = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            createdFiles = fileService.createAll(files, FileConstants.REQUESTS_DIR);
            request.addFiles(createdFiles);
        }
        Request created = requestRepository.save(request);


        requestLogService.create(
                created,
                created.getUser(),
                LogAction.REQUEST_CREATED,
                null,
                "Created",
                "Your request was created"
        );

        createdFiles.forEach(f -> {
            requestLogService.create(
                    created,
                    created.getUser(),
                    LogAction.FILE_UPLOADED,
                    null,
                    f.getOriginalFileName(),
                    "File uploaded to request."
            );
        });

        aiSuggestionService.suggest(request);
        eventPublisher.publishEvent(new RequestCreatedEvent(created.getUser(), created.getId(), created.getTitle()));
        return created;
    }

    @Override
    public Page<RequestPageableProjection> findAll(User user, RequestFilterDto requestFilterDto) {
        requestFilterDto.normalizeTextFields();
        Pageable pageable = requestFilterDto.toPageable();

        RequestScopeFilters scopeFilters = accessScopeService.getRequestFilters(
                user,
                requestFilterDto.getCitizenId(),
                requestFilterDto.getDepartmentId(),
                requestFilterDto.getMunicipalityId(),
                requestFilterDto.getAssignedEmployeeUserId()
        );

        log.info(
                "Filters: id={}, citizenId={}, departmentId={}, municipalityId={}, categoryId={}, status={}, routingStatus={}, priority={}, text={}, submittedFrom={}, submittedTo={}",
                requestFilterDto.getId(),
                scopeFilters.requestedUserId(),
                scopeFilters.departmentId(),
                scopeFilters.municipalityId(),
                requestFilterDto.getCategoryId(),
                requestFilterDto.getStatus(),
                requestFilterDto.getRoutingStatus(),
                requestFilterDto.getPriority(),
                requestFilterDto.getText(),
                requestFilterDto.getSubmittedFrom(),
                requestFilterDto.getSubmittedTo()
        );

        return requestRepository.findFiltered(
                requestFilterDto.getId(),
                scopeFilters.requestedUserId(),
                scopeFilters.departmentId(),
                scopeFilters.municipalityId(),
                requestFilterDto.getCategoryId(),
                scopeFilters.assignedEmployeeUserId(),
                requestFilterDto.getStatus(),
                requestFilterDto.getRoutingStatus(),
                requestFilterDto.getPriority(),
                requestFilterDto.getText(),
                requestFilterDto.getSubmittedFrom(),
                requestFilterDto.getSubmittedTo(),
                pageable
        );
    }

    @Override
    public Request cancel(Long id, User user) {
        Request request = requestRepository
                .findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));

        accessScopeService.hasAccessToRequest(user, request);

        if (request.getStatus() != RequestStatus.SUBMITTED) {
            throw new RequestCannotBeCanceled(id);
        }

        RequestStatus oldStatus = request.getStatus();

        request.setStatus(RequestStatus.CANCELED);

        Request savedRequest = requestRepository.save(request);

        requestLogService.create(
                savedRequest,
                user,
                LogAction.REQUEST_CANCELED,
                oldStatus.name(),
                RequestStatus.CANCELED.name(),
                "Request was canceled by the citizen."
        );

        eventPublisher.publishEvent(new RequestStatusChangedEvent(
                savedRequest.getUser(), savedRequest.getId(), savedRequest.getTitle(), RequestStatus.CANCELED.name()
        ));

        return savedRequest;
    }

    @Override
    public Request changeStatus(Request request, User user, ChangeRequestStatusDto dto) {
        accessScopeService.hasAccessToRequest(user, request);

        RequestStatus oldStatus = request.getStatus();
        RequestStatus newStatus = dto.status();

        validateStatusTransition(oldStatus, newStatus);

        request.setStatus(newStatus);

        Request savedRequest = requestRepository.save(request);

        requestLogService.create(
                savedRequest,
                user,
                LogAction.STATUS_CHANGED,
                oldStatus.name(),
                newStatus.name(),
                dto.note() != null ? dto.note() : dto.reason()
        );
        eventPublisher.publishEvent(new RequestStatusChangedEvent(
                savedRequest.getUser(), savedRequest.getId(), savedRequest.getTitle(), newStatus.name()
        ));

        return savedRequest;
    }

    @Override
    public void delete(Long id) {
        requestRepository.deleteById(id);
    }

    @Override
    public void deleteBulk(List<Long> ids) {
        requestRepository.deleteAllByIdInBatch(ids);
    }


    private void validateStatusTransition(RequestStatus oldStatus, RequestStatus newStatus) {
        if (oldStatus == newStatus) {
            return;
        }

        switch (oldStatus) {
            case SUBMITTED -> {
                if (newStatus != RequestStatus.IN_REVIEW &&
                        newStatus != RequestStatus.CANCELED &&
                        newStatus != RequestStatus.REJECTED) {
                    throw new InvalidRequestStatusTransitionException(oldStatus, newStatus);
                }
            }

            case IN_REVIEW -> {
                if (newStatus != RequestStatus.ASSIGNED &&
                        newStatus != RequestStatus.IN_PROGRESS &&
                        newStatus != RequestStatus.REJECTED) {
                    throw new InvalidRequestStatusTransitionException(oldStatus, newStatus);
                }
            }

            case ASSIGNED -> {
                if (newStatus != RequestStatus.IN_PROGRESS &&
                        newStatus != RequestStatus.REJECTED) {
                    throw new InvalidRequestStatusTransitionException(oldStatus, newStatus);
                }
            }

            case IN_PROGRESS -> {
                if (newStatus != RequestStatus.RESOLVED &&
                        newStatus != RequestStatus.REJECTED) {
                    throw new InvalidRequestStatusTransitionException(oldStatus, newStatus);
                }
            }

            case RESOLVED, REJECTED, CANCELED -> {
                throw new InvalidRequestStatusTransitionException(oldStatus, newStatus);
            }
        }
    }
}
