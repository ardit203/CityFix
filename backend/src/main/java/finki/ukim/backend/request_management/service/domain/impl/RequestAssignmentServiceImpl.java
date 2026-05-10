package finki.ukim.backend.request_management.service.domain.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.RequestAssignmentScopeFilters;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.StaffScope;
import finki.ukim.backend.auth_and_access.service.domain.AccessScopeService;
import finki.ukim.backend.common.exception.ForbiddenException;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.domain.RequestAssignment;
import finki.ukim.backend.request_management.model.dto.filter.RequestAssignmentFilterDto;
import finki.ukim.backend.request_management.model.projection.RequestAssignmentPageableProjection;
import finki.ukim.backend.request_management.repository.RequestAssignmentRepository;
import finki.ukim.backend.request_management.service.domain.RequestAssignmentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestAssignmentServiceImpl implements RequestAssignmentService {
    private final RequestAssignmentRepository requestAssignmentRepository;
    private final AccessScopeService accessScopeService;

    @Override
    public List<RequestAssignment> findAllByRequest(Request request, User user) {
        List<Long> ids = accessScopeService.assignmentsYouCanView(user, request);
        return requestAssignmentRepository.findAllById(ids);
    }

    @Override
    public Page<RequestAssignmentPageableProjection> findAll(Request request, User user, RequestAssignmentFilterDto filters) {
        accessScopeService.hasAccessToRequest(user, request);

        RequestAssignmentScopeFilters scopeFilters =
                accessScopeService.getRequestAssignmentFilters(
                        user,
                        filters.getEmployeeId(),
                        filters.getAssignedByUserId()
                );

        return requestAssignmentRepository.findFiltered(
                request.getId(),
                filters.getId(),
                scopeFilters.employeeId(),
                scopeFilters.assignedByUserId(),
                filters.getAssignedFrom(),
                filters.getAssignedTo(),
                filters.toPageable()
        );
    }

    @Override
    public RequestAssignment assignEmployee(Request request, User user, RequestAssignment assignment) {
        accessScopeService.hasAccessToRequest(user, request);
        StaffScope staffScope = accessScopeService.getStaffScope(assignment.getEmployee());
        accessScopeService.hasAccessToStaff(user, staffScope.staffId());
        return requestAssignmentRepository.save(assignment);
    }

    @Override
    public List<RequestAssignment> assignMultipleEmployees(Request request, User user, List<RequestAssignment> assignments) {
        return List.of();
    }

    @Override
    public void removeAssignment(Request request, User user, RequestAssignment assignment) {
        accessScopeService.hasAccessToRequest(user, request);
        StaffScope staffScope = accessScopeService.getStaffScope(assignment.getEmployee());
        accessScopeService.hasAccessToStaff(user, staffScope.staffId());
        requestAssignmentRepository.delete(assignment);
    }

    @Override
    public void removeMultipleAssignments(Request request, User user, List<Long> ids) {

    }

    @Override
    public void removeAllAssignments(Request request, User user) {

    }
}
