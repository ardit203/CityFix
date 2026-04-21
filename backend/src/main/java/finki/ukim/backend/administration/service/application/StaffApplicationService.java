package finki.ukim.backend.administration.service.application;

import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.model.dto.CreateStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayStaffDto;

import java.util.List;
import java.util.Optional;

public interface StaffApplicationService {
    List<DisplayBasicStaffDto> findAll();

    List<DisplayStaffDto> findAllWithAll();

    Optional<DisplayStaffDto> findById(Long id);

    Optional<DisplayStaffDto> find(CreateStaffDto createStaffDto);

    Optional<DisplayStaffDto> findByUsername(String username);

    List<DisplayBasicStaffDto> findByDepartmentId(Long departmentId);

    List<DisplayBasicStaffDto> findByMunicipalityId(Long municipalityId);

    DisplayStaffDto create(CreateStaffDto createStaffDto);

    Optional<DisplayBasicStaffDto> update(Long id, CreateStaffDto createStaffDto);

    Optional<DisplayBasicStaffDto> deleteById(Long id);
}
