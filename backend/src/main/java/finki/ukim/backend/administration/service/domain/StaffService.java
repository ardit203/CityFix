package finki.ukim.backend.administration.service.domain;

import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.model.projection.StaffPageableProjection;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.projection.UserPageableProjection;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StaffService {
    List<Staff> findAll(User currentUser);

    List<UserPageableProjection> findUsersAvailableForStaff(User currentUser);

    List<Staff> findAllWithAll(User currentUser);

    Staff findById(Long id, User currentUser);

    Staff find(Long userId, Long departmentId, Long municipalityId);

    Staff findByUserId(User currentUser, Long userId);

    List<Staff> findByDepartmentId(Long departmentId);

    List<Staff> findByMunicipalityId(Long municipalityId);

    Staff create(User currentUser, Staff staff);

    Staff update(Long id, Staff updatedStaff);

    Staff deleteById(Long id, User currentUser);

    Page<StaffPageableProjection> findAll(
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
}
