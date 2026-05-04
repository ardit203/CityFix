package finki.ukim.backend.request_management.repository;

import finki.ukim.backend.request_management.model.domain.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
}
