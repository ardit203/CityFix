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
import java.util.List;

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

    @Query("""
            select count(distinct r.id)
            from Request r
            left join r.municipality m
            left join r.category c
            left join r.department d
            where (:citizenId is null or r.user.id = :citizenId)
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
    Long countFiltered(
            @Param("citizenId") Long citizenId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId,
            @Param("categoryId") Long categoryId,
            @Param("status") RequestStatus status,
            @Param("routingStatus") RoutingStatus routingStatus,
            @Param("priority") Priority priority,
            @Param("text") String text,
            @Param("submittedFrom") LocalDateTime submittedFrom,
            @Param("submittedTo") LocalDateTime submittedTo
    );

    @Query("""
            select count(distinct r.id)
            from Request r
            left join r.municipality m
            left join r.category c
            left join r.department d
            where (:citizenId is null or r.user.id = :citizenId)
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
              and exists (
                    select a.id
                    from RequestAssignment a
                    where a.request.id = r.id
                  )
            """)
    Long countAssignedFiltered(
            @Param("citizenId") Long citizenId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId,
            @Param("categoryId") Long categoryId,
            @Param("status") RequestStatus status,
            @Param("routingStatus") RoutingStatus routingStatus,
            @Param("priority") Priority priority,
            @Param("text") String text,
            @Param("submittedFrom") LocalDateTime submittedFrom,
            @Param("submittedTo") LocalDateTime submittedTo
    );

    @Query("""
            select r.status, count(distinct r.id)
            from Request r
            left join r.municipality m
            left join r.category c
            left join r.department d
            where (:citizenId is null or r.user.id = :citizenId)
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
            group by r.status
            """)
    List<Object[]> countByStatus(
            @Param("citizenId") Long citizenId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId,
            @Param("categoryId") Long categoryId,
            @Param("status") RequestStatus status,
            @Param("routingStatus") RoutingStatus routingStatus,
            @Param("priority") Priority priority,
            @Param("text") String text,
            @Param("submittedFrom") LocalDateTime submittedFrom,
            @Param("submittedTo") LocalDateTime submittedTo
    );

    @Query("""
            select r.priority, count(distinct r.id)
            from Request r
            left join r.municipality m
            left join r.category c
            left join r.department d
            where (:citizenId is null or r.user.id = :citizenId)
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
            group by r.priority
            """)
    List<Object[]> countByPriority(
            @Param("citizenId") Long citizenId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId,
            @Param("categoryId") Long categoryId,
            @Param("status") RequestStatus status,
            @Param("routingStatus") RoutingStatus routingStatus,
            @Param("priority") Priority priority,
            @Param("text") String text,
            @Param("submittedFrom") LocalDateTime submittedFrom,
            @Param("submittedTo") LocalDateTime submittedTo
    );

    @Query("""
            select r.routingStatus, count(distinct r.id)
            from Request r
            left join r.municipality m
            left join r.category c
            left join r.department d
            where (:citizenId is null or r.user.id = :citizenId)
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
            group by r.routingStatus
            """)
    List<Object[]> countByRoutingStatus(
            @Param("citizenId") Long citizenId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId,
            @Param("categoryId") Long categoryId,
            @Param("status") RequestStatus status,
            @Param("routingStatus") RoutingStatus routingStatus,
            @Param("priority") Priority priority,
            @Param("text") String text,
            @Param("submittedFrom") LocalDateTime submittedFrom,
            @Param("submittedTo") LocalDateTime submittedTo
    );

    @Query("""
            select coalesce(d.name, 'Unassigned'), count(distinct r.id)
            from Request r
            left join r.municipality m
            left join r.category c
            left join r.department d
            where (:citizenId is null or r.user.id = :citizenId)
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
            group by d.name
            """)
    List<Object[]> countByDepartment(
            @Param("citizenId") Long citizenId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId,
            @Param("categoryId") Long categoryId,
            @Param("status") RequestStatus status,
            @Param("routingStatus") RoutingStatus routingStatus,
            @Param("priority") Priority priority,
            @Param("text") String text,
            @Param("submittedFrom") LocalDateTime submittedFrom,
            @Param("submittedTo") LocalDateTime submittedTo
    );

    @Query("""
            select coalesce(m.code, 'Unassigned'), count(distinct r.id)
            from Request r
            left join r.municipality m
            left join r.category c
            left join r.department d
            where (:citizenId is null or r.user.id = :citizenId)
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
            group by m.code
            """)
    List<Object[]> countByMunicipality(
            @Param("citizenId") Long citizenId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId,
            @Param("categoryId") Long categoryId,
            @Param("status") RequestStatus status,
            @Param("routingStatus") RoutingStatus routingStatus,
            @Param("priority") Priority priority,
            @Param("text") String text,
            @Param("submittedFrom") LocalDateTime submittedFrom,
            @Param("submittedTo") LocalDateTime submittedTo
    );

    @Query("""
            select coalesce(c.name, 'Unassigned'), count(distinct r.id)
            from Request r
            left join r.municipality m
            left join r.category c
            left join r.department d
            where (:citizenId is null or r.user.id = :citizenId)
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
            group by c.name
            """)
    List<Object[]> countByCategory(
            @Param("citizenId") Long citizenId,
            @Param("departmentId") Long departmentId,
            @Param("municipalityId") Long municipalityId,
            @Param("categoryId") Long categoryId,
            @Param("status") RequestStatus status,
            @Param("routingStatus") RoutingStatus routingStatus,
            @Param("priority") Priority priority,
            @Param("text") String text,
            @Param("submittedFrom") LocalDateTime submittedFrom,
            @Param("submittedTo") LocalDateTime submittedTo
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
