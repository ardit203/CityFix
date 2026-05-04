package finki.ukim.backend.request_management.repository;

import finki.ukim.backend.request_management.model.domain.RequestComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestCommentRepository extends JpaRepository<RequestComment, Long> {
}
