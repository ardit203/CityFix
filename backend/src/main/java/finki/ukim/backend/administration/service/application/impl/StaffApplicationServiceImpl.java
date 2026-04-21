package finki.ukim.backend.administration.service.application.impl;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.model.dto.CreateStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayStaffDto;
import finki.ukim.backend.administration.model.exception.DepartmentNotFoundException;
import finki.ukim.backend.administration.service.application.StaffApplicationService;
import finki.ukim.backend.administration.service.domain.DepartmentService;
import finki.ukim.backend.administration.service.domain.MunicipalityService;
import finki.ukim.backend.administration.service.domain.StaffService;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.exception.UserNotFoundException;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StaffApplicationServiceImpl implements StaffApplicationService {
    private final StaffService staffService;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final MunicipalityService municipalityService;

    @Override
    public List<DisplayBasicStaffDto> findAll() {
        return DisplayBasicStaffDto.from(staffService.findAll());
    }

    @Override
    public List<DisplayStaffDto> findAllWithAll() {
        return DisplayStaffDto.from(staffService.findAllWithAll());
    }

    @Override
    public Optional<DisplayStaffDto> findById(Long id) {
        return staffService.findById(id).map(DisplayStaffDto::from);
    }

    @Override
    public Optional<DisplayStaffDto> find(CreateStaffDto createStaffDto) {
        return staffService.find(createStaffDto.username(), createStaffDto.departmentId(), createStaffDto.municipalityId()).map(DisplayStaffDto::from);
    }

    @Override
    public Optional<DisplayStaffDto> findByUsername(String username) {
        return staffService.findByUsername(username).map(DisplayStaffDto::from);
    }

    @Override
    public List<DisplayBasicStaffDto> findByDepartmentId(Long departmentId) {
        return DisplayBasicStaffDto.from(staffService.findByDepartmentId(departmentId));
    }

    @Override
    public List<DisplayBasicStaffDto> findByMunicipalityId(Long municipalityId) {
        return DisplayBasicStaffDto.from(staffService.findByMunicipalityId(municipalityId));
    }

    @Override
    public DisplayStaffDto create(CreateStaffDto createStaffDto) {
        User user = userService.findByUsername(createStaffDto.username()).orElseThrow(() -> new UserNotFoundException(createStaffDto.username()));

        Department department = departmentService.findById(createStaffDto.departmentId()).orElseThrow(() -> new DepartmentNotFoundException(createStaffDto.departmentId()));

        Municipality municipality = municipalityService.findById(createStaffDto.municipalityId()).orElseThrow(() -> new DepartmentNotFoundException(createStaffDto.municipalityId()));

        return DisplayStaffDto.from(staffService.create(createStaffDto.toStaff(user, department, municipality)));
    }

    @Override
    public Optional<DisplayBasicStaffDto> update(Long id, CreateStaffDto createStaffDto) {
        User user = userService.findByUsername(createStaffDto.username()).orElseThrow(() -> new UserNotFoundException(createStaffDto.username()));

        Department department = departmentService.findById(createStaffDto.departmentId()).orElseThrow(() -> new DepartmentNotFoundException(createStaffDto.departmentId()));

        Municipality municipality = municipalityService.findById(createStaffDto.municipalityId()).orElseThrow(() -> new DepartmentNotFoundException(createStaffDto.municipalityId()));

        return staffService.update(id, createStaffDto.toStaff(user, department, municipality)).map(DisplayBasicStaffDto::from);
    }

    @Override
    public Optional<DisplayBasicStaffDto> deleteById(Long id) {
        return staffService.deleteById(id).map(DisplayBasicStaffDto::from);
    }
}
