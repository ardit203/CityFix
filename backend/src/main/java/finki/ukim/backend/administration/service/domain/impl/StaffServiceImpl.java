package finki.ukim.backend.administration.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.model.exception.CitizenCannotBeStaffException;
import finki.ukim.backend.administration.model.exception.StaffNotFoundException;
import finki.ukim.backend.administration.model.exception.UserIsNotStaffException;
import finki.ukim.backend.administration.model.projection.StaffPageableProjection;
import finki.ukim.backend.administration.repository.StaffRepository;
import finki.ukim.backend.administration.service.domain.StaffService;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    @Override
    public List<Staff> findAll(User currentUser) {
        if (currentUser.getRole() == Role.ROLE_ADMINISTRATOR) {
            return staffRepository.findAll();
        }

        if (currentUser.getRole() == Role.ROLE_MANAGER) {
            Municipality managerMunicipality = getManagerMunicipality(currentUser);
            return staffRepository.findByMunicipalityId(managerMunicipality.getId());
        }

        throw new AccessDeniedException("You are not allowed to view staff.");
    }

    @Override
    public List<Staff> findAllWithAll(User currentUser) {
        if (currentUser.getRole() == Role.ROLE_ADMINISTRATOR) {
            return staffRepository.findAllWithAll();
        }

        if (currentUser.getRole() == Role.ROLE_MANAGER) {
            Municipality managerMunicipality = getManagerMunicipality(currentUser);
            return staffRepository.findAllWithAllByMunicipalityId(managerMunicipality.getId());
        }

        throw new AccessDeniedException("You are not allowed to view staff.");
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
    public Staff find(User currentUser, Long userId, Long departmentId, Long municipalityId) {
        Long effectiveMunicipalityId = municipalityId;

        if (currentUser.getRole() == Role.ROLE_MANAGER) {
            effectiveMunicipalityId = getManagerMunicipality(currentUser).getId();
        }

        if (currentUser.getRole() != Role.ROLE_ADMINISTRATOR
                && currentUser.getRole() != Role.ROLE_MANAGER) {
            throw new AccessDeniedException("You are not allowed to search staff.");
        }

        return staffRepository
                .find(userId, departmentId, effectiveMunicipalityId)
                .orElseThrow();
    }

    @Override
    public Staff findByUserId(User currentUser, Long userId) {
        Staff staff = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new UserIsNotStaffException(userId));

        checkStaffAccess(currentUser, staff);

        return staff;
    }

    @Override
    public List<Staff> findByDepartmentId(User currentUser, Long departmentId) {
        if (currentUser.getRole() == Role.ROLE_ADMINISTRATOR) {
            return staffRepository.findByDepartmentId(departmentId);
        }

        if (currentUser.getRole() == Role.ROLE_MANAGER) {
            Municipality managerMunicipality = getManagerMunicipality(currentUser);
            return staffRepository.findByDepartmentIdAndMunicipalityId(
                    departmentId,
                    managerMunicipality.getId()
            );
        }

        throw new AccessDeniedException("You are not allowed to view staff.");
    }

    @Override
    public List<Staff> findByMunicipalityId(User currentUser, Long municipalityId) {
        if (currentUser.getRole() == Role.ROLE_ADMINISTRATOR) {
            return staffRepository.findByMunicipalityId(municipalityId);
        }

        if (currentUser.getRole() == Role.ROLE_MANAGER) {
            Municipality managerMunicipality = getManagerMunicipality(currentUser);
            return staffRepository.findByMunicipalityId(managerMunicipality.getId());
        }

        throw new AccessDeniedException("You are not allowed to view staff.");
    }

    @Override
    public Staff create(User currentUser, Staff staff) {
        if (currentUser.getRole() != Role.ROLE_ADMINISTRATOR) {
            throw new AccessDeniedException("Only administrator can create staff.");
        }

        if (staff.getUser().getRole() == Role.ROLE_CITIZEN) {
            throw new CitizenCannotBeStaffException(staff.getUser().getUsername());
        }

        return staffRepository.save(staff);
    }

    @Override
    public Staff update(Long id, User currentUser, Staff updatedStaff) {
        Staff existingStaff = findById(id, currentUser);

        if (currentUser.getRole() == Role.ROLE_MANAGER) {
            Municipality managerMunicipality = getManagerMunicipality(currentUser);

            if (updatedStaff.getMunicipality() != null
                    && !updatedStaff.getMunicipality().getId().equals(managerMunicipality.getId())) {
                throw new AccessDeniedException("Manager cannot move staff to another municipality.");
            }
        }

        if (updatedStaff.getUser() != null) {
            if (updatedStaff.getUser().getRole() == Role.ROLE_CITIZEN) {
                throw new CitizenCannotBeStaffException(updatedStaff.getUser().getUsername());
            }
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
        Staff staff = findById(id, currentUser);

        if (currentUser.getRole() != Role.ROLE_ADMINISTRATOR) {
            throw new AccessDeniedException("Only administrator can delete staff.");
        }

        staffRepository.delete(staff);
        return staff;
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
                Sort.by(sortBy).and(Sort.by("createdAt"))
        );

        Long effectiveMunicipalityId = municipalityId;

        if (currentUser.getRole() == Role.ROLE_MANAGER) {
            effectiveMunicipalityId = getManagerMunicipality(currentUser).getId();
        } else if (currentUser.getRole() != Role.ROLE_ADMINISTRATOR) {
            throw new AccessDeniedException("You are not allowed to view staff.");
        }

        return staffRepository.findFiltered(
                id,
                userId,
                departmentId,
                effectiveMunicipalityId,
                username,
                municipalityCode,
                municipalityName,
                pageable
        );
    }

    private Municipality getManagerMunicipality(User currentUser) {
        Staff managerStaff = staffRepository.findByUserIdWithMunicipality(currentUser.getId())
                .orElseThrow(() -> new UserIsNotStaffException(currentUser.getId()));

        return managerStaff.getMunicipality();
    }

    private void checkStaffAccess(User currentUser, Staff staff) {
        if (currentUser.getRole() == Role.ROLE_ADMINISTRATOR) {
            return;
        }

        if (currentUser.getRole() == Role.ROLE_MANAGER) {
            Municipality managerMunicipality = getManagerMunicipality(currentUser);

            if (staff.getMunicipality().getId().equals(managerMunicipality.getId())) {
                return;
            }
        }

        throw new AccessDeniedException("You are not allowed to access this staff member.");
    }
}