package finki.ukim.backend.administration.service.application.impl;

import finki.ukim.backend.administration.model.domain.Department;
import finki.ukim.backend.administration.model.domain.Municipality;
import finki.ukim.backend.administration.model.dto.CreateStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayStaffPageableDto;
import finki.ukim.backend.administration.model.exception.DepartmentNotFoundException;
import finki.ukim.backend.administration.service.application.StaffApplicationService;
import finki.ukim.backend.administration.service.domain.DepartmentService;
import finki.ukim.backend.administration.service.domain.MunicipalityService;
import finki.ukim.backend.administration.service.domain.StaffService;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.DisplayUserPageableDto;
import finki.ukim.backend.auth_and_access.model.exception.UserNotFoundException;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
    public List<DisplayBasicStaffDto> findAll(User currentUser) {
        return DisplayBasicStaffDto.from(staffService.findAll(currentUser));
    }

    @Override
    public List<DisplayStaffDto> findAllWithAll(User currentUser) {
        return DisplayStaffDto.from(staffService.findAllWithAll(currentUser));
    }

    @Override
    public List<DisplayUserPageableDto> findUsersAvailableForStaff(User currentUser) {
        return DisplayUserPageableDto.from(staffService.findUsersAvailableForStaff(currentUser));
    }

    @Override
    public Page<DisplayStaffPageableDto> findAll(
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
        return staffService
                .findAll(currentUser, page, size, sortBy, id, userId, departmentId, municipalityId, username, municipalityCode, municipalityName)
                .map(DisplayStaffPageableDto::from);
    }

    @Override
    public DisplayStaffDto findById(Long id, User currentUser) {
        return DisplayStaffDto.from(staffService.findById(id, currentUser));
    }

    @Override
    public DisplayStaffDto find(CreateStaffDto createStaffDto) {
        return DisplayStaffDto.from(
                staffService.find(createStaffDto.userId(), createStaffDto.departmentId(), createStaffDto.municipalityId())
        );
    }

    @Override
    public DisplayStaffDto findByUserId(User currentUser, Long userId) {
        return DisplayStaffDto.from(staffService.findByUserId(currentUser, userId));
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
    public DisplayStaffDto create(User currentUser, CreateStaffDto createStaffDto) {
        User user = userService.findById(createStaffDto.userId());

        Department department = departmentService.findById(createStaffDto.departmentId());

        Municipality municipality = municipalityService.findById(createStaffDto.municipalityId());

        return DisplayStaffDto.from(staffService.create(currentUser, createStaffDto.toStaff(user, department, municipality)));
    }

    @Override
    public DisplayBasicStaffDto update(Long id, CreateStaffDto createStaffDto) {
        User user = userService.findById(createStaffDto.userId());

        Department department = departmentService.findById(createStaffDto.departmentId());

        Municipality municipality = municipalityService.findById(createStaffDto.municipalityId());

        return DisplayBasicStaffDto.from(staffService.update(id, createStaffDto.toStaff(user, department, municipality)));
    }

    @Override
    public DisplayBasicStaffDto deleteById(Long id, User currentUser) {
        return DisplayBasicStaffDto.from(staffService.deleteById(id, currentUser));
    }
}
