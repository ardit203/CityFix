package finki.ukim.backend.request_management.repository;

import finki.ukim.backend.request_management.model.domain.RequestComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestCommentRepository extends JpaRepository<RequestComment, Long> {
    List<RequestComment> findAllByRequestId(Long requestId);
    List<RequestComment> findAllByRequestIdAndIsInternalFalse(Long requestId);
}
