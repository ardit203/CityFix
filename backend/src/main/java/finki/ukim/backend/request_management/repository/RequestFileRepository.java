package finki.ukim.backend.request_management.repository;

import finki.ukim.backend.request_management.model.domain.RequestFile;
import finki.ukim.backend.request_management.model.projection.RequestFileProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestFileRepository extends JpaRepository<RequestFile, Long> {
    @Query("""
            select rf.id as id,
                   rf.file.id as fileId,
                   rf.request.id as requestId,
                   rf.file.fileName as fileName,
                   rf.file.fileUrl as fileUrl
            from RequestFile rf
            where rf.id = :id
            """)
    Optional<RequestFileProjection> findProjectedById(@Param("id") Long id);

    @Query("""
            select rf.id as id,
                   rf.file.id as fileId,
                   rf.request.id as requestId,
                   rf.file.fileName as fileName,
                   rf.file.fileUrl as fileUrl
            from RequestFile rf
            where rf.request.id = :requestId
            """)
    List<RequestFileProjection> findAllProjectedByRequestId(@Param("requestId") Long requestId);
}
