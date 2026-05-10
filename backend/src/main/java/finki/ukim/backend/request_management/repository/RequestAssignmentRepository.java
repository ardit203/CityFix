package finki.ukim.backend.request_management.repository;

import finki.ukim.backend.request_management.model.domain.RequestAssignment;
import finki.ukim.backend.request_management.model.projection.RequestAssignmentPageableProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestAssignmentRepository extends JpaRepository<RequestAssignment, Long> {
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
               r.title as requestTitle,
               r.priority as requestPriority,
               r.status as requestStatus,
               employee.username as employeeUsername,
               assignedBy.username as assignedByUsername,
               a.assignedAt as assignedAt
        from RequestAssignment a
        join a.request r
        join a.employee employee
        join a.assignedBy assignedBy
        where (:requestId is null or r.id = :requestId)
          and (:id is null or a.id = :id)
          and (:employeeId is null or employee.id = :employeeId)
          and (:assignedByUserId is null or assignedBy.id = :assignedByUserId)
          and (:assignedFrom is null or a.assignedAt >= :assignedFrom)
          and (:assignedTo is null or a.assignedAt <= :assignedTo)
        """)
    Page<RequestAssignmentPageableProjection> findFiltered(
            @Param("requestId") Long requestId,
            @Param("id") Long id,
            @Param("employeeId") Long employeeId,
            @Param("assignedByUserId") Long assignedByUserId,
            @Param("assignedFrom") LocalDateTime assignedFrom,
            @Param("assignedTo") LocalDateTime assignedTo,
            Pageable pageable
    );
}
