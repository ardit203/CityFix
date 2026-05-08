package finki.ukim.backend.administration.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.model.dto.filters.StaffFilterDto;
import finki.ukim.backend.administration.model.exception.*;
import finki.ukim.backend.administration.model.projection.StaffPageableProjection;
import finki.ukim.backend.administration.repository.StaffRepository;
import finki.ukim.backend.administration.service.domain.StaffService;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.StaffScope;
import finki.ukim.backend.auth_and_access.model.dto.accessScope.StaffScopeFilters;
import finki.ukim.backend.auth_and_access.model.projection.UserPageableProjection;
import finki.ukim.backend.auth_and_access.service.domain.AccessScopeService;
import finki.ukim.backend.common.helper.FilterUtils;
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
    private final AccessScopeService accessScopeService;
    private final StaffRepository staffRepository;

    @Override
    public List<Staff> findAll(User currentUser) {
        if (accessScopeService.isAdmin(currentUser)) {
            return staffRepository.findAll();
        }

        if (accessScopeService.isManager(currentUser)) {
            StaffScope managerScope = accessScopeService.getStaffScope(currentUser);

            return staffRepository.findByMunicipality_IdAndDepartment_Id(
                    managerScope.municipalityId(),
                    managerScope.departmentId()
            );
        }

        throw new InsufficientRoleException(currentUser.getUsername());
    }

    @Override
    public List<UserPageableProjection> findUsersAvailableForStaff(User currentUser) {
        return staffRepository.findUsersWithRoleAndNotStaff(
                accessScopeService.getRoleVisibility(currentUser)
        );
    }

    @Override
    public List<Staff> findAllWithAll(User currentUser) {
        if (accessScopeService.isAdmin(currentUser)) {
            return staffRepository.findAllWithAll();
        }

        if (accessScopeService.isManager(currentUser)) {
            StaffScope managerStaff = accessScopeService.getStaffScope(currentUser);

            return staffRepository.findAllWithAllByMunicipalityIdAndDepartmentId(
                    managerStaff.municipalityId(),
                    managerStaff.departmentId()
            );
        }

        throw new InsufficientRoleException(currentUser.getUsername());
    }

    @Override
    public Staff findById(Long id, User currentUser) {
        Staff staff = staffRepository
                .findStaffById(id)
                .orElseThrow(() -> new StaffNotFoundException(id));

        accessScopeService.hasAccessToStaff(currentUser, staff);

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

        accessScopeService.hasAccessToStaff(currentUser, staff);

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
        accessScopeService.canBeStaff(staff.getUser());
        accessScopeService.hasAccessToStaff(currentUser, staff);
        return staffRepository.save(staff);
    }

    @Override
    public Staff update(Long id, Staff updatedStaff) {
        Staff existingStaff = staffRepository
                .findStaffById(id)
                .orElseThrow(() -> new StaffNotFoundException(id));

        if (updatedStaff.getUser() != null) {
            accessScopeService.canBeStaff(updatedStaff.getUser());
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

        accessScopeService.hasAccessToStaff(currentUser, staff);

        staffRepository.delete(staff);
        return staff;
    }

    @Override
    public Page<StaffPageableProjection> findAll(
            User currentUser,
            StaffFilterDto staffFilterDto
    ) {
        staffFilterDto.normalizeTextFields();
        Pageable pageable = staffFilterDto.toPageable();

        StaffScopeFilters staffScopeFilters = accessScopeService.getStaffFilters(
                currentUser, staffFilterDto.getDepartmentId(), staffFilterDto.getMunicipalityId()
        );


        return staffRepository.findFiltered(
                staffFilterDto.getId(),
                staffFilterDto.getUserId(),
                staffScopeFilters.departmentId(),
                staffScopeFilters.municipalityId(),
                staffFilterDto.getUsername(),
                staffFilterDto.getMunicipalityCode(),
                staffFilterDto.getMunicipalityName(),
                pageable
        );
    }
}