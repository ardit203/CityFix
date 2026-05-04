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
    @Query("""
            select s from Staff s
            join fetch s.user
            join fetch s.department
            join fetch s.municipality
            """)
    List<Staff> findAllWithAll();


    @EntityGraph(attributePaths = {"user", "department", "municipality"})
//    @Query("""
//            select s from Staff s
//            join fetch s.user
//            join fetch s.department
//            join fetch s.municipality
//            where s.id = :id
//            """)
    Optional<Staff> findStaffById(@Param("id") Long id);

    /// //////////////////////////////////////////////////////////////////////////////////////
    @EntityGraph(attributePaths = {"user", "department", "municipality"})
    @Query("""
            select s from Staff s
            where s.user.id = :userId
            """)
    Optional<Staff> findByUserId(@Param("userId") Long userId);

    /// //////////////////////////////////////////////////////////////////////////////////////


    @Query("""
            select s from Staff s
            where s.department.id = :departmentId
            """)
    List<Staff> findByDepartmentId(@Param("departmentId") Long departmentId);

    /// //////////////////////////////////////////////////////////////////////////////////////

    List<Staff> findByMunicipality_Id(Long municipalityId);

    List<Staff> findByMunicipality_IdAndDepartment_Id(Long municipalityId, Long departmentId);

    /// ///////////////////////////////////////////////////////////////////////////////////
    @Query("""
            select s from Staff s
            join fetch s.user
            join fetch s.department
            join fetch s.municipality
            where s.user.id = :userId
            and s.department.id = :departmentId
            and s.municipality.id = :municipalityId""")
    Optional<Staff> find(
            @Param("userId") Long userId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId
    );


    @Query("""
                select s.id as id,
                       p.name as name,
                       p.surname as surname,
                       u.email as email,
                       u.username as username,
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
                        :username is null
                        or lower(u.username) like lower(concat('%', :username, '%'))
                      )
                  and (
                        :municipalityCode is null
                        or lower(m.code) like lower(concat('%', :municipalityCode, '%'))
                      )
                  and (
                        :municipalityName is null
                        or lower(m.name) like lower(concat('%', :municipalityName, '%'))
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

    @EntityGraph(attributePaths = {"user", "department", "municipality"})
    @Query("""
                select s from Staff s
                where s.municipality.id = :municipalityId
            """)
    List<Staff> findAllWithAllByMunicipalityIdAndDepartmentId(
            @Param("municipalityId") Long municipalityId,
            @Param("departmentId") Long departmentId
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


    @EntityGraph(attributePaths = {"department", "municipality"})
    @Query("""
            select s
            from Staff s
            where s.user.id = :userId
            """)
    Optional<Staff> findByUserIdWithDepartmentAndMunicipality(@Param("userId") Long userId);

    boolean existsByUser_Id(Long userId);
}
