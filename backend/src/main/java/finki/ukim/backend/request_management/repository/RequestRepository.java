package finki.ukim.backend.request_management.repository;

import finki.ukim.backend.request_management.model.domain.Request;
import finki.ukim.backend.request_management.model.enums.Priority;
import finki.ukim.backend.request_management.model.enums.RequestStatus;
import finki.ukim.backend.request_management.model.enums.RoutingStatus;
import finki.ukim.backend.request_management.model.projection.RequestPageableProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("""
            select distinct
                      r.id as id,
                      r.title as title,
                      r.priority as priority,
                      r.status as status,
                      r.routingStatus as routingStatus,
                      m.code as municipalityCode,
                      c.name as categoryName,
                      d.name as departmentName
            from Request r
            left join r.municipality m
            left join r.category c
            left join r.department d
            where (:id is null or r.id = :id)
              and (:citizenId is null or r.user.id = :citizenId)
              and (:departmentId is null or d.id = :departmentId)
              and (:municipalityId is null or m.id = :municipalityId)
              and (:categoryId is null or c.id = :categoryId)
              and (:status is null or r.status = :status)
              and (:routingStatus is null or r.routingStatus = :routingStatus)
              and (:priority is null or r.priority = :priority)
              and (
                    :text = ''
                    or lower(r.title) like concat('%', :text, '%')
                    or lower(r.description) like concat('%', :text, '%')
                    or lower(r.summary) like concat('%', :text, '%')
                  )
              and (:submittedFrom is null or r.createdAt >= :submittedFrom)
              and (:submittedTo is null or r.createdAt <= :submittedTo)
            """)
    Page<RequestPageableProjection> findFiltered(
            @Param("id") Long id,
            @Param("citizenId") Long citizenId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId,
            @Param("categoryId") Long categoryId,
            @Param("status") RequestStatus status,
            @Param("routingStatus") RoutingStatus routingStatus,
            @Param("priority") Priority priority,
            @Param("text") String text,
            @Param("submittedFrom") LocalDateTime submittedFrom,
            @Param("submittedTo") LocalDateTime submittedTo,
            Pageable pageable
    );


    //    and (
//                    :assignedEmployeeUserId is null
//                    or exists (
//                    select 1
//                    from RequestAssignment ra
//                    where ra.request.id = r.id
//                    and ra.employeeUser.id = :assignedEmployeeUserId
//    )
//                  )
}
