package finki.ukim.backend.request_management.repository;

import finki.ukim.backend.request_management.model.domain.RequestFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestFileRepository extends JpaRepository<RequestFile, Long> {
}
