package finki.ukim.backend.request_management.repository;

import finki.ukim.backend.request_management.model.domain.RequestAssignment;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import finki.ukim.backend.request_management.model.projection.RequestAssignmentPageableProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RequestAssignmentRepository extends JpaRepository<RequestAssignment, Long> {
    @EntityGraph(attributePaths = {"employee", "employee.profile", "assignedBy", "assignedBy.profile"})
    List<RequestAssignment> findAllByRequest_Id(Long requestId);

    @EntityGraph(attributePaths = {"request", "employee", "employee.profile", "assignedBy", "assignedBy.profile"})
    List<RequestAssignment> findByIdIn(List<Long> ids);

    @EntityGraph(attributePaths = {
            "request",
            "request.department",
            "request.municipality",
            "employee",
            "employee.profile",
            "assignedBy",
            "assignedBy.profile"
    })
    Optional<RequestAssignment> findWithDetailsById(Long id);

    Boolean existsByRequest_IdAndEmployee_Id(Long request_Id, Long employee_Id);

    @Query("""
            select a.id
            from RequestAssignment a
            where (:requestId is null or a.request.id = :requestId)
              and (:departmentId is null or a.request.department.id = :departmentId)
              and (:municipalityId is null or a.request.municipality.id = :municipalityId)
              and (:employeeId is null or a.employee.id = :employeeId)
            """)
    List<Long> findIds(
            @Param("requestId") Long requestId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId,
            @Param("employeeId") Long employeeId
    );

    @Query("""
            select a.id as id,
                   r.id as requestId,
                   r.title as requestTitle,
                   r.priority as requestPriority,
                   r.status as requestStatus,
                   d.name as departmentName,
                   employee.id as employeeId,
                   employee.username as employeeUsername,
                   employeeProfile.name as employeeName,
                   employeeProfile.surname as employeeSurname,
                   assignedBy.id as assignedByUserId,
                   assignedBy.username as assignedByUsername,
                   assignedByProfile.name as assignedByName,
                   assignedByProfile.surname as assignedBySurname,
                   a.assignedAt as assignedAt
            from RequestAssignment a
            join a.request r
            left join r.department d
            join a.employee employee
            join employee.profile employeeProfile
            join a.assignedBy assignedBy
            join assignedBy.profile assignedByProfile
            where (:requestId is null or r.id = :requestId)
              and (:id is null or a.id = :id)
              and (:employeeId is null or employee.id = :employeeId)
              and (:assignedByUserId is null or assignedBy.id = :assignedByUserId)
              and (:departmentId is null or r.department.id = :departmentId)
              and (:municipalityId is null or r.municipality.id = :municipalityId)
              and (:requestStatus is null or r.status = :requestStatus)
              and (:assignedFrom is null or a.assignedAt >= :assignedFrom)
              and (:assignedTo is null or a.assignedAt <= :assignedTo)
            """)
    Page<RequestAssignmentPageableProjection> findFiltered(
            @Param("requestId") Long requestId,
            @Param("id") Long id,
            @Param("employeeId") Long employeeId,
            @Param("assignedByUserId") Long assignedByUserId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId,
            @Param("requestStatus") RequestStatus requestStatus,
            @Param("assignedFrom") LocalDateTime assignedFrom,
            @Param("assignedTo") LocalDateTime assignedTo,
            Pageable pageable
    );
}
