package finki.ukim.backend.request_management.repository;

import finki.ukim.backend.request_management.model.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
