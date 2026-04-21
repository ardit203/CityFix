package finki.ukim.backend.administration.service.domain;

import finki.ukim.backend.administration.model.domain.Staff;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    List<Staff> findAll();

    List<Staff> findAllWithAll();

    Optional<Staff> findById(Long id);

    Optional<Staff> find(String username, Long departmentId, Long municipalityId);

    Optional<Staff> findByUsername(String username);

    List<Staff> findByDepartmentId(Long departmentId);

    List<Staff> findByMunicipalityId(Long municipalityId);

    Staff create(Staff staff);

    Optional<Staff> update(Long id, Staff updatedStaff);

    Optional<Staff> deleteById(Long id);
}
