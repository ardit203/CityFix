package finki.ukim.backend.administration.repository;

import finki.ukim.backend.administration.model.domain.Staff;
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

    @Query("""
            select s from Staff s
            join fetch s.user
            join fetch s.department
            join fetch s.municipality
            where s.id = :id
            """)
    Optional<Staff> findStaffById(@Param("id") Long id);

    /// //////////////////////////////////////////////////////////////////////////////////////
    @Query("""
            select s from Staff s
            where s.user.username = :username
            """)
    Optional<Staff> findByUsername(@Param("username") String username);

    @Query("""
            select s from Staff s
                        join fetch s.user
                        join fetch s.department
                        join fetch s.municipality
            where s.user.username = :username
            """)
    Optional<Staff> findByUsernameWithAll(@Param("username") String username);

    /// //////////////////////////////////////////////////////////////////////////////////////


    @Query("""
            select s from Staff s
            where s.department.id = :departmentId
            """)
    List<Staff> findByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("""
            select s from Staff s
                        join fetch s.user
                        join fetch s.department
                        join fetch s.municipality
            where s.department.id = :departmentId
            """)
    List<Staff> findByDepartmentIdWithAll(@Param("departmentId") Long departmentId);

    /// //////////////////////////////////////////////////////////////////////////////////////

    @Query("""
            select s from Staff s
            where s.municipality.id = :municipalityId
            """)
    List<Staff> findByMunicipalityId(@Param("municipalityId") Long municipalityId);

    @Query("""
            select s from Staff s
                        join fetch s.user
                        join fetch s.department
                        join fetch s.municipality
            where s.municipality.id = :municipalityId
            """)
    List<Staff> findByMunicipalityIdWithAll(@Param("municipalityId") Long municipalityId);

    /// ///////////////////////////////////////////////////////////////////////////////////
    @Query("""
            select s from Staff s
            join fetch s.user
            join fetch s.department
            join fetch s.municipality
            where s.user.username = :username
            and s.department.id = :departmentId
            and s.municipality.id = :municipalityId""")
    Optional<Staff> find(
            @Param("username") String username,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId
    );
}
