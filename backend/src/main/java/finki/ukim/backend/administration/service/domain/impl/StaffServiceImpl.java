package finki.ukim.backend.administration.service.domain.impl;

import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.model.exception.CitizenCannotBeStaffException;
import finki.ukim.backend.administration.repository.StaffRepository;
import finki.ukim.backend.administration.service.domain.StaffService;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;

    @Override
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    @Override
    public List<Staff> findAllWithAll() {
        return staffRepository.findAllWithAll();
    }

    @Override
    public Optional<Staff> findById(Long id) {
        return staffRepository.findStaffById(id);
    }

    @Override
    public Optional<Staff> find(String username, Long departmentId, Long municipalityId) {
        return staffRepository.find(username, departmentId, municipalityId);
    }

    @Override
    public Optional<Staff> findByUsername(String username) {
        return staffRepository.findByUsername(username);
    }

    @Override
    public List<Staff> findByDepartmentId(Long departmentId) {
        return staffRepository.findByDepartmentId(departmentId);
    }

    @Override
    public List<Staff> findByMunicipalityId(Long municipalityId) {
        return staffRepository.findByMunicipalityId(municipalityId);
    }

    @Override
    public Staff create(Staff staff) {
        if (staff.getUser().getRole() == Role.ROLE_CITIZEN) {
            throw new CitizenCannotBeStaffException(staff.getUser().getUsername());
        }
        return staffRepository.save(staff);
    }

    @Override
    public Optional<Staff> update(Long id, Staff updatedStaff) {
        return findById(id)
                .map(existingStaff -> {
                    if (updatedStaff.getUser().getRole() == Role.ROLE_CITIZEN) {
                        throw new CitizenCannotBeStaffException(updatedStaff.getUser().getUsername());
                    }
                    existingStaff.setUser(updatedStaff.getUser());
                    existingStaff.setDepartment(updatedStaff.getDepartment());
                    existingStaff.setMunicipality(updatedStaff.getMunicipality());
                    return staffRepository.save(existingStaff);
                });
    }

    @Override
    public Optional<Staff> deleteById(Long id) {
        Optional<Staff> staff = findById(id);
        staff.ifPresent(staffRepository::delete);
        return staff;
    }
}
