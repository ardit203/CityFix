package finki.ukim.backend.administration.service.application;

import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.model.dto.CreateStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayStaffPageableDto;
import finki.ukim.backend.administration.model.dto.filters.StaffFilterDto;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.dto.DisplayUserPageableDto;
import finki.ukim.backend.auth_and_access.model.projection.UserPageableProjection;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface StaffApplicationService {
    List<DisplayBasicStaffDto> findAll(User user);

    List<DisplayStaffDto> findAllWithAll(User user);

    List<DisplayUserPageableDto> findUsersAvailableForStaff(User currentUser);

    Page<DisplayStaffPageableDto> findAll(
            User user,
            StaffFilterDto staffFilterDto
    );

    DisplayStaffDto findById(Long id, User user);

    DisplayStaffDto find(CreateStaffDto createStaffDto);

    DisplayStaffDto findByUserId(User user, Long userId);

    List<DisplayBasicStaffDto> findByDepartmentId(Long departmentId);

    List<DisplayBasicStaffDto> findByMunicipalityId(Long municipalityId);

    DisplayStaffDto create(User user, CreateStaffDto createStaffDto);

    DisplayBasicStaffDto update(Long id, CreateStaffDto createStaffDto);

    DisplayBasicStaffDto deleteById(Long id, User user);

    void deleteAllById(List<Long> ids, User user);
}
