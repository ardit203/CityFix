package finki.ukim.backend.request_management.service.domain.impl;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.RequestAssignmentScopeFilters;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.StaffScope;
import finki.ukim.backend.auth_and_access.service.domain.AccessScopeService;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.domain.RequestAssignment;
import finki.ukim.backend.request_management.model.dto.filter.RequestAssignmentFilterDto;
import finki.ukim.backend.request_management.model.enums.LogAction;
import finki.ukim.backend.request_management.model.exception.RequestAssignmentNotFoundException;
import finki.ukim.backend.request_management.model.projection.RequestAssignmentPageableProjection;
import finki.ukim.backend.request_management.repository.RequestAssignmentRepository;
import finki.ukim.backend.request_management.service.domain.RequestAssignmentService;
import finki.ukim.backend.request_management.service.domain.RequestLogService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestAssignmentServiceImpl implements RequestAssignmentService {
    private final RequestAssignmentRepository requestAssignmentRepository;
    private final AccessScopeService accessScopeService;
    private final RequestLogService requestLogService;

    @Override
    public RequestAssignment findById(Long id) {
        return requestAssignmentRepository.findById(id)
                .orElseThrow(() -> new RequestAssignmentNotFoundException(id));
    }

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

        assignment.setRequest(request);
        assignment.setAssignedBy(user);

        RequestAssignment created = requestAssignmentRepository.save(assignment);

        requestLogService.create(
                request,
                user,
                LogAction.EMPLOYEE_ASSIGNED,
                null,
                formatUser(created.getEmployee()),
                "Employee assigned to request."
        );

        return created;
    }

//    @Override
//    public List<RequestAssignment> assignMultipleEmployees(Request request, User user, List<RequestAssignment> assignments) {
//        return List.of();
//    }

    @Override
    public void removeAssignment(Request request, User user, RequestAssignment assignment) {
        accessScopeService.hasAccessToRequest(user, request);

        StaffScope staffScope = accessScopeService.getStaffScope(assignment.getEmployee());
        accessScopeService.hasAccessToStaff(user, staffScope.staffId());

        requestLogService.create(
                request,
                user,
                LogAction.EMPLOYEE_REMOVED,
                formatUser(assignment.getEmployee()),
                null,
                "Employee removed from request."
        );

        requestAssignmentRepository.delete(assignment);
    }

    @Override
    public void removeMultipleAssignments(Request request, User user, List<Long> ids) {
        accessScopeService.hasAccessToRequest(user, request);

        List<RequestAssignment> assignments = requestAssignmentRepository.findAllById(ids);

        List<RequestAssignment> validAssignments = assignments.stream()
                .filter(assignment -> assignment.getRequest().getId().equals(request.getId()))
                .toList();

        List<Long> validIds = validAssignments.stream()
                .map(RequestAssignment::getId)
                .toList();

        String removedEmployees = validAssignments.stream()
                .map(RequestAssignment::getEmployee)
                .map(this::formatUser)
                .collect(Collectors.joining(", "));

        if (!validAssignments.isEmpty()) {
            requestLogService.create(
                    request,
                    user,
                    LogAction.EMPLOYEE_REMOVED,
                    removedEmployees,
                    null,
                    validAssignments.size() + " employee assignment(s) removed from request."
            );
        }

        requestAssignmentRepository.deleteAllByIdInBatch(validIds);
    }

    @Override
    public void removeAllAssignments(Request request, User user) {
        accessScopeService.hasAccessToRequest(user, request);

        List<RequestAssignment> assignments = requestAssignmentRepository.findAllByRequest_Id(request.getId());

        String removedEmployees = assignments.stream()
                .map(RequestAssignment::getEmployee)
                .map(this::formatUser)
                .collect(Collectors.joining(", "));

        if (!assignments.isEmpty()) {
            requestLogService.create(
                    request,
                    user,
                    LogAction.EMPLOYEE_REMOVED,
                    removedEmployees,
                    null,
                    "All employee assignments removed from request."
            );
        }

        List<Long> ids = assignments.stream()
                .map(RequestAssignment::getId)
                .toList();

        requestAssignmentRepository.deleteAllByIdInBatch(ids);
    }





    private String formatUser(User user) {
        if (user == null) {
            return null;
        }

        return user.getId() + " - " + user.getUsername();
    }

    private String formatUsers(List<User> users) {
        return users.stream()
                .map(this::formatUser)
                .collect(Collectors.joining(", "));
    }
}
