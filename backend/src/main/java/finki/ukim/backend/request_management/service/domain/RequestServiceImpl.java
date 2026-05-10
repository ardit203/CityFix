package finki.ukim.backend.request_management.service.domain;

import finki.ukim.backend.administration.model.exception.InsufficientRoleException;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.RequestScopeFilters;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.StaffScope;
import finki.ukim.backend.auth_and_access.service.domain.AccessScopeService;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.dto.filter.RequestFilterDto;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import finki.ukim.backend.request_management.model.exception.RequestCannotBeCanceled;
import finki.ukim.backend.request_management.model.exception.RequestNotFoundException;
import finki.ukim.backend.request_management.model.projection.RequestPageableProjection;
import finki.ukim.backend.request_management.repository.RequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final AccessScopeService accessScopeService;

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
    public Request create(Request request) {
        Request created = requestRepository.save(request);
        //start ai
        //send mail
        return created;
    }

    @Override
    public Page<RequestPageableProjection> findAll(User user, RequestFilterDto requestFilterDto) {
        requestFilterDto.normalizeTextFields();
        Pageable pageable = requestFilterDto.toPageable();
        List<Request> requests = findAll(user);
        System.out.println(requests);

        RequestScopeFilters scopeFilters = accessScopeService.getRequestFilters(
                user,
                requestFilterDto.getCitizenId(),
                requestFilterDto.getDepartmentId(),
                requestFilterDto.getMunicipalityId(),
                requestFilterDto.getAssignedEmployeeUserId()
        );

        Page<RequestPageableProjection> page = requestRepository.findFiltered(
                requestFilterDto.getId(),
                scopeFilters.requestedUserId(),
                scopeFilters.departmentId(),
                scopeFilters.municipalityId(),
                requestFilterDto.getCategoryId(),
//                scopeFilters.assignedEmployeeUserId(),
                requestFilterDto.getStatus(),
                requestFilterDto.getRoutingStatus(),
                requestFilterDto.getPriority(),
                requestFilterDto.getText(),
                requestFilterDto.getSubmittedFrom(),
                requestFilterDto.getSubmittedTo(),
                pageable
        );

        return page;
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

        request.setStatus(RequestStatus.CANCELED);
        return requestRepository.save(request);
    }

    @Override
    public void delete(Long id) {
        requestRepository.deleteById(id);
    }

    @Override
    public void deleteBulk(List<Long> ids) {
        requestRepository.deleteAllByIdInBatch(ids);
    }
}
