package finki.ukim.backend.auth_and_access.service.domain;

import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.RequestAssignmentScopeFilters;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.RequestScopeFilters;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.StaffScope;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.StaffScopeFilters;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.request_management.model.domain.Request;

import java.util.List;

public interface AccessScopeService {

    boolean isAdmin(User currentUser);

    boolean isManager(User currentUser);

    boolean isEmployee(User currentUser);

    boolean isCitizen(User currentUser);

    boolean isStaff(User currentUser);

    boolean isManagement(User currentUser);

    List<Role> getRoleVisibility(User currentUser);

    void checkForManagement(User currentUser);

    Staff getCurrentStaff(User currentUser);

    StaffScope getStaffScope(User currentUser);

    void canBeStaff(User currentUser);

    void hasAccessToStaff(User currentUser, Staff targetStaff);

    void hasAccessToStaff(User currentUser, Long staffId);

    void hasAccessToRequest(User currentUser, Request request);

    List<Long> assignmentsYouCanView(User currentUser, Request request);

    StaffScopeFilters getStaffFilters(
            User currentUser,
            Long departmentId,
            Long municipalityId
    );

    RequestScopeFilters getRequestFilters(
            User currentUser,
            Long requestedUserId,
            Long departmentId,
            Long municipalityId,
            Long assignedEmployeeUserId
    );

    RequestAssignmentScopeFilters getRequestAssignmentFilters(
            User currentUser,
            Long employeeId,
            Long assignedByUserId,
            Long departmentId,
            Long municipalityId
    );
}
