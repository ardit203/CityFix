package finki.ukim.backend.request_management.repository;

import finki.ukim.backend.request_management.model.domain.RequestLog;
import finki.ukim.backend.request_management.model.enums.LogAction;
import finki.ukim.backend.request_management.model.projection.RequestLogPageableProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {

    @EntityGraph(attributePaths = {"request"})
    @Query("select l from RequestLog l where l.id = :id")
    Optional<RequestLog> findByIdWithRequest(@Param("id") Long id);

    @Query("select l from RequestLog l where l.request.id = :requestId")
    List<RequestLog> findAllByRequestId(@Param("requestId") Long requestId);

    @Query("""
            select l.id as id,
                   l.action as action,
                   l.oldValue as oldValue,
                   l.newValue as newValue,
                   l.note as note,
                   l.createdAt as createdAt
            from RequestLog l
            where (:id is null or l.id = :id)
              and (:requestId is null or l.request.id = :requestId)
              and (:actionByUserId is null or l.actionBy.id = :actionByUserId)
              and (:action is null or l.action = :action)
              and (
                    :text = ''
                    or lower(coalesce(l.oldValue, '')) like concat('%', :text, '%')
                    or lower(coalesce(l.newValue, '')) like concat('%', :text, '%')
                    or lower(coalesce(l.note, '')) like concat('%', :text, '%')
                  )
              and (:createdFrom is null or l.createdAt >= :createdFrom)
              and (:createdTo is null or l.createdAt <= :createdTo)
            """)
    Page<RequestLogPageableProjection> findFiltered(
            @Param("id") Long id,
            @Param("requestId") Long requestId,
            @Param("actionByUserId") Long actionByUserId,
            @Param("action") LogAction action,
            @Param("text") String text,
            @Param("createdFrom") LocalDateTime createdFrom,
            @Param("createdTo") LocalDateTime createdTo,
            Pageable pageable
    );
}

