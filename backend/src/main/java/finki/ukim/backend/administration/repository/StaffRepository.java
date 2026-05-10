package finki.ukim.backend.administration.repository;

import finki.ukim.backend.administration.model.domain.Staff;
import finki.ukim.backend.administration.model.projection.StaffPageableProjection;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.projection.UserPageableProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    @EntityGraph(attributePaths = {"user", "department", "municipality"})
    @Query("select s from Staff s")
    List<Staff> findAllWithAll();

    @EntityGraph(attributePaths = {"user", "department", "municipality"})
    Optional<Staff> findStaffById(@Param("id") Long id);

    @EntityGraph(attributePaths = {"user", "department", "municipality"})
    @Query("""
            select s from Staff s
            where s.user.id = :userId
            """)
    Optional<Staff> findByUserId(@Param("userId") Long userId);

    @Query("""
            select s from Staff s
            where s.user.id = :userId
            """)
    Optional<Staff> findByUserIdLazy(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user", "department", "municipality"})
    @Query("""
            select s from Staff s
            where s.user.id = :userId
              and s.department.id = :departmentId
              and s.municipality.id = :municipalityId
            """)
    Optional<Staff> find(
            @Param("userId") Long userId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId
    );

    List<Staff> findByDepartment_Id(Long departmentId);

    List<Staff> findByMunicipality_Id(Long municipalityId);

    List<Staff> findByMunicipality_IdAndDepartment_Id(
            Long municipalityId,
            Long departmentId
    );

    @EntityGraph(attributePaths = {"user", "department", "municipality"})
    @Query("""
            select s from Staff s
            where s.municipality.id = :municipalityId
              and s.department.id = :departmentId
            """)
    List<Staff> findAllWithAllByMunicipalityIdAndDepartmentId(
            @Param("municipalityId") Long municipalityId,
            @Param("departmentId") Long departmentId
    );

    @Query("""
            select s.id as id,
                   u.id as userId,
                   p.name as name,
                   p.surname as surname,
                   u.email as email,
                   u.username as username,
                   u.role as role,
                   d.name as departmentName,
                   m.name as municipalityName,
                   m.code as municipalityCode
            from Staff s
            join s.user u
            join u.profile p
            join s.department d
            join s.municipality m
            where (:id is null or s.id = :id)
              and (:userId is null or u.id = :userId)
              and (:departmentId is null or d.id = :departmentId)
              and (:municipalityId is null or m.id = :municipalityId)
              and (
                    :username = ''
                    or lower(u.username) like concat('%', :username, '%')
                  )
              and (
                    :municipalityCode = ''
                    or lower(m.code) like concat('%', :municipalityCode, '%')
                  )
              and (
                    :municipalityName = ''
                    or lower(m.name) like concat('%', :municipalityName, '%')
                  )
            """)
    Page<StaffPageableProjection> findFiltered(
            @Param("id") Long id,
            @Param("userId") Long userId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId,
            @Param("username") String username,
            @Param("municipalityCode") String municipalityCode,
            @Param("municipalityName") String municipalityName,
            Pageable pageable
    );

    @Query("""
            select u.id as id,
                   u.username as username,
                   u.role as role,
                   p.name as name,
                   p.surname as surname
            from User u
            left join u.profile p
            where u.role in :roles
              and not exists (
                  select s.id
                  from Staff s
                  where s.user.id = u.id
              )
            """)
    List<UserPageableProjection> findUsersWithRoleAndNotStaff(
            @Param("roles") List<Role> roles
    );


    @Query("""
            select s
            from Staff s
            where s.user.id in :ids
            """)
    List<Staff> findStaff(
            @Param("ids") List<Long> ids
    );

    boolean existsByUser_Id(Long userId);
}