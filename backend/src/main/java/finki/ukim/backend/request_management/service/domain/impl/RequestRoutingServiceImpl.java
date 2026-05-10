package finki.ukim.backend.request_management.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Category;
import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.repository.CategoryRepository;
import finki.ukim.backend.administration.repository.DepartmentRepository;
import finki.ukim.backend.administration.repository.MunicipalityRepository;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.service.domain.AccessScopeService;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.dto.RejectRoutingDto;
import finki.ukim.backend.request_management.model.dto.UpdateRequestRoutingDto;
import finki.ukim.backend.request_management.model.enums.LogAction;
import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RoutingStatus;
import finki.ukim.backend.request_management.repository.RequestRepository;
import finki.ukim.backend.request_management.service.domain.RequestLogService;
import finki.ukim.backend.request_management.service.domain.RequestRoutingService;
import finki.ukim.backend.request_management.service.domain.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestRoutingServiceImpl implements RequestRoutingService {

    private final RequestService requestService;
    private final RequestRepository requestRepository;
    private final RequestLogService requestLogService;
    private final AccessScopeService accessScopeService;

    private final CategoryRepository categoryRepository;
    private final DepartmentRepository departmentRepository;
    private final MunicipalityRepository municipalityRepository;

    @Override
    public Request updateRouting(Long requestId, User currentUser, UpdateRequestRoutingDto dto) {
        accessScopeService.checkForManagement(currentUser);
        Request request = requestService.findById(requestId, currentUser);

        if (dto.categoryId() != null && (request.getCategory() == null || !request.getCategory().getId().equals(dto.categoryId()))) {
            String oldVal = request.getCategory() != null ? request.getCategory().getName() : "None";
            Category category = categoryRepository.findById(dto.categoryId()).orElseThrow();
            request.setCategory(category);
            requestLogService.create(request, currentUser, LogAction.CATEGORY_CHANGED, oldVal, category.getName(), dto.note());
        }

        if (dto.departmentId() != null && (request.getDepartment() == null || !request.getDepartment().getId().equals(dto.departmentId()))) {
            String oldVal = request.getDepartment() != null ? request.getDepartment().getName() : "None";
            Department department = departmentRepository.findById(dto.departmentId()).orElseThrow();
            request.setDepartment(department);
            requestLogService.create(request, currentUser, LogAction.DEPARTMENT_CHANGED, oldVal, department.getName(), dto.note());
        }

        if (dto.municipalityId() != null && (request.getMunicipality() == null || !request.getMunicipality().getId().equals(dto.municipalityId()))) {
            Municipality municipality = municipalityRepository.findById(dto.municipalityId()).orElseThrow();
            request.setMunicipality(municipality);
            // Municipality doesn't have a specific log action requested, but we could log it.
        }

        if (dto.priority() != null && request.getPriority() != dto.priority()) {
            String oldVal = request.getPriority() != null ? request.getPriority().name() : "None";
            request.setPriority(dto.priority());
            requestLogService.create(request, currentUser, LogAction.PRIORITY_CHANGED, oldVal, dto.priority().name(), dto.note());
        }

        if (dto.summary() != null && !dto.summary().equals(request.getSummary())) {
            request.setSummary(dto.summary());
        }

        // Whenever routing is manually updated, we return it to PENDING_REVIEW if it was rejected or confirmed
        if (request.getRoutingStatus() != RoutingStatus.PENDING_REVIEW) {
            String oldVal = request.getRoutingStatus() != null ? request.getRoutingStatus().name() : "None";
            request.setRoutingStatus(RoutingStatus.PENDING_REVIEW);
            requestLogService.create(request, currentUser, LogAction.ROUTING_REOPENED, oldVal, RoutingStatus.PENDING_REVIEW.name(), "Routing updated manually, returning to pending review.");
        }

        return requestRepository.save(request);
    }

    @Override
    public Request confirmRouting(Long requestId, User currentUser) {
        accessScopeService.checkForManagement(currentUser);
        Request request = requestService.findById(requestId, currentUser);

        String oldVal = request.getRoutingStatus() != null ? request.getRoutingStatus().name() : "None";
        request.setRoutingStatus(RoutingStatus.CONFIRMED);

        requestLogService.create(request, currentUser, LogAction.ROUTING_CONFIRMED, oldVal, RoutingStatus.CONFIRMED.name(), "Routing confirmed by manager.");

        return requestRepository.save(request);
    }

    @Override
    public Request rejectRouting(Long requestId, User currentUser, RejectRoutingDto dto) {
        accessScopeService.checkForManagement(currentUser);
        Request request = requestService.findById(requestId, currentUser);

        String oldVal = request.getRoutingStatus() != null ? request.getRoutingStatus().name() : "None";
        request.setRoutingStatus(RoutingStatus.REJECTED);

        requestLogService.create(request, currentUser, LogAction.ROUTING_REJECTED, oldVal, RoutingStatus.REJECTED.name(), dto.reason());

        return requestRepository.save(request);
    }

    @Override
    public Request reopenRouting(Long requestId, User currentUser) {
        accessScopeService.checkForManagement(currentUser);
        Request request = requestService.findById(requestId, currentUser);

        String oldVal = request.getRoutingStatus() != null ? request.getRoutingStatus().name() : "None";
        request.setRoutingStatus(RoutingStatus.PENDING_REVIEW);

        requestLogService.create(request, currentUser, LogAction.ROUTING_REOPENED, oldVal, RoutingStatus.PENDING_REVIEW.name(), "Routing reopened because request may belong to another department.");

        return requestRepository.save(request);
    }
}
