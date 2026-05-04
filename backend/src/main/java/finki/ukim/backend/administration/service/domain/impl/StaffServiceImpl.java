package finki.ukim.backend.administration.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.model.exception.*;
import finki.ukim.backend.administration.model.projection.StaffPageableProjection;
import finki.ukim.backend.administration.repository.StaffRepository;
import finki.ukim.backend.administration.service.domain.StaffService;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.projection.UserPageableProjection;
import finki.ukim.backend.common.exception.ForbiddenException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    @Override
    public List<Staff> findAll(User currentUser) {
        if (isAdmin(currentUser)) {
            return staffRepository.findAll();
        }

        if (isManager(currentUser)) {
            Staff managerStaff = getManagerStaff(currentUser);

            return staffRepository.findByMunicipality_IdAndDepartment_Id(
                    managerStaff.getMunicipality().getId(),
                    managerStaff.getDepartment().getId()
            );
        }

        throw new InsufficientRoleException(currentUser.getUsername());
    }

    @Override
    public List<UserPageableProjection> findUsersAvailableForStaff(User currentUser) {
        List<Role> roles;

        if (isAdmin(currentUser)) {
            roles = List.of(Role.ROLE_MANAGER, Role.ROLE_EMPLOYEE);
        } else if (isManager(currentUser)) {
            roles = List.of(Role.ROLE_EMPLOYEE);
        } else {
            throw new InsufficientRoleException(currentUser.getUsername());
        }

        return staffRepository.findUsersWithRoleAndNotStaff(roles);
    }

    @Override
    public List<Staff> findAllWithAll(User currentUser) {
        if (isAdmin(currentUser)) {
            return staffRepository.findAllWithAll();
        }

        if (isManager(currentUser)) {
            Staff managerStaff = getManagerStaff(currentUser);

            return staffRepository.findAllWithAllByMunicipalityIdAndDepartmentId(
                    managerStaff.getMunicipality().getId(),
                    managerStaff.getDepartment().getId()
            );
        }

        throw new InsufficientRoleException(currentUser.getUsername());
    }

    @Override
    public Staff findById(Long id, User currentUser) {
        Staff staff = staffRepository
                .findStaffById(id)
                .orElseThrow(() -> new StaffNotFoundException(id));

        checkStaffAccess(currentUser, staff);

        return staff;
    }

    @Override
    public Staff find(Long userId, Long departmentId, Long municipalityId) {
        return staffRepository
                .find(userId, departmentId, municipalityId)
                .orElseThrow(() -> new StaffNotFoundException(userId, departmentId, municipalityId));
    }

    @Override
    public Staff findByUserId(User currentUser, Long userId) {
        Staff staff = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new UserIsNotStaffException(userId));

        checkStaffAccess(currentUser, staff);

        return staff;
    }

    @Override
    public List<Staff> findByDepartmentId(Long departmentId) {
        return staffRepository.findByDepartmentId(departmentId);
    }

    @Override
    public List<Staff> findByMunicipalityId(Long municipalityId) {
        return staffRepository.findByMunicipality_Id(municipalityId);
    }

    @Override
    public Staff create(User currentUser, Staff staff) {
        validateUserCanBeStaff(staff);

        if (staffRepository.existsByUser_Id(staff.getUser().getId())) {
            throw new UserIsAlreadyStaffException(staff.getUser().getId());
        }

        if (isAdmin(currentUser)) {
            return staffRepository.save(staff);
        }

        if (isManager(currentUser)) {
            Staff managerStaff = getManagerStaff(currentUser);

            checkManagerCreateAccess(managerStaff, staff);

            return staffRepository.save(staff);
        }

        throw new InsufficientRoleException(currentUser.getUsername());
    }

    @Override
    public Staff update(Long id, Staff updatedStaff) {
        Staff existingStaff = staffRepository
                .findStaffById(id)
                .orElseThrow(() -> new StaffNotFoundException(id));

        if (updatedStaff.getUser() != null) {
            validateUserCanBeStaff(updatedStaff);
            existingStaff.setUser(updatedStaff.getUser());
        }

        if (updatedStaff.getDepartment() != null) {
            existingStaff.setDepartment(updatedStaff.getDepartment());
        }

        if (updatedStaff.getMunicipality() != null) {
            existingStaff.setMunicipality(updatedStaff.getMunicipality());
        }
        return staffRepository.save(existingStaff);

    }

    @Override
    public Staff deleteById(Long id, User currentUser) {
        Staff staff = staffRepository
                .findStaffById(id)
                .orElseThrow(() -> new StaffNotFoundException(id));

        if (isAdmin(currentUser)) {
            staffRepository.delete(staff);
            return staff;
        }

        if (isManager(currentUser)) {
            Staff managerStaff = getManagerStaff(currentUser);

            checkManagerStaffAccess(managerStaff, staff);

            staffRepository.delete(staff);
            return staff;
        }

        throw new InsufficientRoleException(currentUser.getUsername());
    }

    @Override
    public Page<StaffPageableProjection> findAll(
            User currentUser,
            int page,
            int size,
            String sortBy,
            Long id,
            Long userId,
            Long departmentId,
            Long municipalityId,
            String username,
            String municipalityCode,
            String municipalityName
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy)
        );

        Long effectiveMunicipalityId = municipalityId;
        Long effectiveDepartmentId = departmentId;

        if (isManager(currentUser)) {
            Staff managerStaff = getManagerStaff(currentUser);

            effectiveMunicipalityId = managerStaff.getMunicipality().getId();
            effectiveDepartmentId = managerStaff.getDepartment().getId();

        } else if (!isAdmin(currentUser)) {
            throw new InsufficientRoleException(currentUser.getUsername());
        }

        return staffRepository.findFiltered(
                id,
                userId,
                effectiveDepartmentId,
                effectiveMunicipalityId,
                username,
                municipalityCode,
                municipalityName,
                pageable
        );
    }

    private Staff getManagerStaff(User currentUser) {
        return staffRepository.findByUserIdWithDepartmentAndMunicipality(currentUser.getId())
                .orElseThrow(() -> new UserIsNotStaffException(currentUser.getUsername()));
    }

    private void checkStaffAccess(User currentUser, Staff targetStaff) {
        if (isAdmin(currentUser)) {
            return;
        }

        if (isManager(currentUser)) {
            Staff managerStaff = getManagerStaff(currentUser);
            checkManagerStaffAccess(managerStaff, targetStaff);
            return;
        }

        throw new InsufficientRoleException(currentUser.getUsername());
    }

    private void checkManagerStaffAccess(Staff managerStaff, Staff targetStaff) {
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

    private void checkManagerCreateAccess(Staff managerStaff, Staff staff) {
        if (staff.getMunicipality() == null || staff.getDepartment() == null) {
            throw new StaffOutsideManagerScopeException();
        }

        boolean sameMunicipality = staff.getMunicipality().getId()
                .equals(managerStaff.getMunicipality().getId());

        boolean sameDepartment = staff.getDepartment().getId()
                .equals(managerStaff.getDepartment().getId());

        if (!sameMunicipality || !sameDepartment) {
            throw new StaffOutsideManagerScopeException();
        }
    }

    private void validateUserCanBeStaff(Staff staff) {
        if (staff.getUser() != null &&
                staff.getUser().getRole() == Role.ROLE_CITIZEN) {
            throw new CitizenCannotBeStaffException(staff.getUser().getUsername());
        }
    }

    private boolean isAdmin(User currentUser) {
        return currentUser.getRole() == Role.ROLE_ADMINISTRATOR;
    }

    private boolean isManager(User currentUser) {
        return currentUser.getRole() == Role.ROLE_MANAGER;
    }
}