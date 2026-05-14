package finki.ukim.backend.auth_and_access.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.model.exception.CitizenCannotBeStaffException;
import finki.ukim.backend.administration.model.exception.InsufficientRoleException;
import finki.ukim.backend.administration.model.exception.UserIsAlreadyStaffException;
import finki.ukim.backend.administration.model.exception.UserIsNotStaffException;
import finki.ukim.backend.administration.repository.StaffRepository;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.RequestAssignmentScopeFilters;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.RequestScopeFilters;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.StaffScope;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.StaffScopeFilters;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.service.domain.AccessScopeService;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import finki.ukim.backend.common.exception.ForbiddenException;
import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.domain.RequestLog;
import finki.ukim.backend.request_management.repository.RequestAssignmentRepository;
import finki.ukim.backend.request_management.repository.RequestLogRepository;
import finki.ukim.backend.request_management.repository.RequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccessScopeServiceImpl implements AccessScopeService {
    private final StaffRepository staffRepository;
    private final RequestAssignmentRepository requestAssignmentRepository;
    private final UserService userService;

    private static final List<Role> STAFF_ROLES = List.of(
            Role.ROLE_MANAGER,
            Role.ROLE_EMPLOYEE
    );

    private static final List<Role> MANAGEMENT_ROLES = List.of(
            Role.ROLE_MANAGER,
            Role.ROLE_ADMINISTRATOR
    );

    @Override
    public boolean isAdmin(User currentUser) {
        return currentUser.getRole() == Role.ROLE_ADMINISTRATOR;
    }

    @Override
    public boolean isManager(User currentUser) {
        return currentUser.getRole() == Role.ROLE_MANAGER;
    }

    @Override
    public boolean isEmployee(User currentUser) {
        return currentUser.getRole() == Role.ROLE_EMPLOYEE;
    }

    @Override
    public boolean isCitizen(User currentUser) {
        return currentUser.getRole() == Role.ROLE_CITIZEN;
    }

    @Override
    public boolean isStaff(User currentUser) {
        return STAFF_ROLES.contains(currentUser.getRole());
    }

    @Override
    public boolean isManagement(User currentUser) {
        return MANAGEMENT_ROLES.contains(currentUser.getRole());
    }

    @Override
    public List<Role> getRoleVisibility(User currentUser) {
        if (isAdmin(currentUser)) {
            return STAFF_ROLES;
        } else if (isManager(currentUser)) {
            return List.of(Role.ROLE_EMPLOYEE);
        } else {
            throw new InsufficientRoleException(currentUser.getUsername());
        }
    }

    @Override
    public void checkForManagement(User currentUser) {
        if (isManagement(currentUser)) {
            return;
        }

        throw new InsufficientRoleException(currentUser.getUsername());
    }

    @Override
    public Staff getCurrentStaff(User currentUser) {
        return staffRepository.findByUserIdLazy(currentUser.getId())
                .orElseThrow(() -> new UserIsNotStaffException(currentUser.getUsername()));
    }

    @Override
    public StaffScope getStaffScope(User currentUser) {
        if (isAdmin(currentUser)) {
            return StaffScope.toAdminScope();
        }

        if (isStaff(currentUser)) {
            Staff staff = getCurrentStaff(currentUser);

            if (staff.getDepartment() == null || staff.getMunicipality() == null) {
                throw new ForbiddenException("Current staff member has no department or municipality assigned.");
            }

            return StaffScope.toStaffScope(
                    staff.getId(),
                    staff.getDepartment().getId(),
                    staff.getMunicipality().getId(),
                    staff.getUser().getId()
            );
        }

        throw new InsufficientRoleException(currentUser.getUsername());
    }

    @Override
    public void canBeStaff(User currentUser) {
        if (isCitizen(currentUser)) {
            throw new CitizenCannotBeStaffException(currentUser.getUsername());
        }

        if (staffRepository.existsByUser_Id(currentUser.getId())) {
            throw new UserIsAlreadyStaffException(currentUser.getId());
        }
    }

    @Override
    public void hasAccessToStaff(User currentUser, Staff targetStaff) {
        if (isAdmin(currentUser)) {
            return;
        }

        if (!isManager(currentUser)) {
            throw new InsufficientRoleException(currentUser.getUsername());
        }

        Staff managerStaff = getCurrentStaff(currentUser);

        if (managerStaff.getMunicipality() == null ||
                managerStaff.getDepartment() == null ||
                targetStaff.getMunicipality() == null ||
                targetStaff.getDepartment() == null) {
            throw new ForbiddenException("You are not allowed to access this staff member.");
        }

        boolean sameMunicipality = targetStaff.getMunicipality().getId()
                .equals(managerStaff.getMunicipality().getId());

        boolean sameDepartment = targetStaff.getDepartment().getId()
                .equals(managerStaff.getDepartment().getId());

        if (!sameMunicipality || !sameDepartment) {
            throw new ForbiddenException("You are not allowed to access this staff member.");
        }
    }

    @Override
    public void hasAccessToStaff(User currentUser, Long staffId) {
        Staff staff = staffRepository
                .findById(staffId)
                .orElseThrow(() -> new UserIsNotStaffException(currentUser.getUsername()));
        hasAccessToStaff(currentUser, staff);
    }

    @Override
    public void hasAccessToRequest(User currentUser, Request request) {
        if (isAdmin(currentUser)) {
            return;
        }

        if (isCitizen(currentUser)) {
            if (request.getUser() != null &&
                    request.getUser().getId().equals(currentUser.getId())) {
                return;
            }

            throw new ForbiddenException("You are not allowed to access this request.");
        }

        if (isManager(currentUser)) {
            StaffScope scope = getStaffScope(currentUser);

            boolean sameDepartment = request.getDepartment() != null &&
                    request.getDepartment().getId().equals(scope.departmentId());

            boolean sameMunicipality = request.getMunicipality() != null &&
                    request.getMunicipality().getId().equals(scope.municipalityId());

            if (sameDepartment && sameMunicipality) {
                return;
            }

            throw new ForbiddenException("You are not allowed to access this request.");
        }

        if (isEmployee(currentUser)) {
            StaffScope scope = getStaffScope(currentUser);

            boolean sameDepartment = request.getDepartment() != null &&
                    request.getDepartment().getId().equals(scope.departmentId());

            boolean sameMunicipality = request.getMunicipality() != null &&
                    request.getMunicipality().getId().equals(scope.municipalityId());

            boolean assignedToEmployee = requestAssignmentRepository
                    .existsByRequest_IdAndEmployee_Id(
                            request.getId(),
                            currentUser.getId()
                    );

            if (sameDepartment && sameMunicipality && assignedToEmployee) {
                return;
            }

            throw new ForbiddenException("You are not allowed to access this request.");
        }

        throw new InsufficientRoleException(currentUser.getUsername());
    }

    @Override
    public List<Long> assignmentsYouCanView(User currentUser, Request request) {
        if (isCitizen(currentUser)) {
            return List.of();
        }
        if (isAdmin(currentUser)) {
            return requestAssignmentRepository.findIds(request.getId(), null, null, null);
        }
        hasAccessToRequest(currentUser, request);

        if (isManager(currentUser)) {
            return requestAssignmentRepository.findIds(
                    request.getId(),
                    request.getDepartment().getId(),
                    request.getMunicipality().getId(),
                    null
            );
        }

        if (isEmployee(currentUser)) {
            return requestAssignmentRepository.findIds(
                    request.getId(),
                    request.getDepartment().getId(),
                    request.getMunicipality().getId(),
                    currentUser.getId()
            );
        }
        throw new InsufficientRoleException(currentUser.getUsername());
    }

    @Override
    public StaffScopeFilters getStaffFilters(
            User currentUser,
            Long departmentId,
            Long municipalityId
    ) {
        if (isAdmin(currentUser)) {
            return new StaffScopeFilters(departmentId, municipalityId);
        }

        if (isManager(currentUser)) {
            StaffScope scope = getStaffScope(currentUser);
            return new StaffScopeFilters(scope.departmentId(), scope.municipalityId());
        }

        throw new InsufficientRoleException(currentUser.getUsername());
    }

    @Override
    public RequestScopeFilters getRequestFilters(
            User currentUser,
            Long requestedUserId,
            Long departmentId,
            Long municipalityId,
            Long assignedEmployeeUserId
    ) {
        if (isAdmin(currentUser)) {
            return new RequestScopeFilters(
                    requestedUserId,
                    departmentId,
                    municipalityId,
                    assignedEmployeeUserId
            );
        }

        if (isCitizen(currentUser)) {
            return new RequestScopeFilters(
                    currentUser.getId(),
                    departmentId,
                    municipalityId,
                    null
            );
        }

        if (isManager(currentUser)) {
            if (assignedEmployeeUserId != null) {
                User employee = userService.findById(assignedEmployeeUserId);
                Staff employeeStaff = getCurrentStaff(employee);
                hasAccessToStaff(currentUser, employeeStaff);
            }
            StaffScope scope = getStaffScope(currentUser);
            return new RequestScopeFilters(
                    requestedUserId,
                    scope.departmentId(),
                    scope.municipalityId(),
                    assignedEmployeeUserId
            );
        }

        if (isEmployee(currentUser)) {
            StaffScope scope = getStaffScope(currentUser);

            return new RequestScopeFilters(
                    requestedUserId,
                    scope.departmentId(),
                    scope.municipalityId(),
                    currentUser.getId()
            );
        }

        throw new InsufficientRoleException(currentUser.getUsername());
    }

    @Override
    public RequestAssignmentScopeFilters getRequestAssignmentFilters(
            User currentUser,
            Long employeeId,
            Long assignedByUserId,
            Long departmentId,
            Long municipalityId
    ) {
        if (isCitizen(currentUser)) {
            throw new ForbiddenException("Citizens are not allowed to view request assignments.");
        }

        if (isAdmin(currentUser)) {
            return new RequestAssignmentScopeFilters(
                    employeeId,
                    assignedByUserId,
                    departmentId,
                    municipalityId
            );
        }

        if (isManager(currentUser)) {
            StaffScope scope = getStaffScope(currentUser);
            return new RequestAssignmentScopeFilters(
                    employeeId,
                    assignedByUserId,
                    scope.departmentId(),
                    scope.municipalityId()
            );
        }

        if (isEmployee(currentUser)) {
            StaffScope scope = getStaffScope(currentUser);
            return new RequestAssignmentScopeFilters(
                    currentUser.getId(),
                    null,
                    scope.departmentId(),
                    scope.municipalityId()
            );
        }

        throw new InsufficientRoleException(currentUser.getUsername());
    }
}
