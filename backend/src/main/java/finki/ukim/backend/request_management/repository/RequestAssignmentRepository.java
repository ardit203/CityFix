package finki.ukim.backend.request_management.repository;

import finki.ukim.backend.request_management.model.domain.RequestAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestAssignmentRepository extends JpaRepository<RequestAssignment, Long> {
}
