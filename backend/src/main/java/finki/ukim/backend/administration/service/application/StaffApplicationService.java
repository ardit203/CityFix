package finki.ukim.backend.administration.service.application;

import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.model.dto.CreateStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayBasicStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayStaffDto;
import finki.ukim.backend.administration.model.dto.DisplayStaffPageableDto;
import finki.ukim.backend.auth_and_access.model.domain.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface StaffApplicationService {
    List<DisplayBasicStaffDto> findAll(User user);

    List<DisplayStaffDto> findAllWithAll(User user);

    Page<DisplayStaffPageableDto> findAll(
            User user,
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
    );

    DisplayStaffDto findById(Long id, User user);

    DisplayStaffDto find(User user, CreateStaffDto createStaffDto);

    DisplayStaffDto findByUserId(User user, Long userId);

    List<DisplayBasicStaffDto> findByDepartmentId(User user, Long departmentId);

    List<DisplayBasicStaffDto> findByMunicipalityId(User user, Long municipalityId);

    DisplayStaffDto create(User user, CreateStaffDto createStaffDto);

    DisplayBasicStaffDto update(Long id, User user, CreateStaffDto createStaffDto);

    DisplayBasicStaffDto deleteById(Long id, User user);
}
