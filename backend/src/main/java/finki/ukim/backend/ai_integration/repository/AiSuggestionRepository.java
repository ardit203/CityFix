package finki.ukim.backend.ai_integration.repository;

import finki.ukim.backend.ai_integration.model.domain.AiSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiSuggestionRepository extends JpaRepository<AiSuggestion, Long> {
    Optional<AiSuggestion> findByRequest_Id(Long requestId);
//    Optional<AiSuggestion> findFirstByRequestIdOrderByCreatedAtDesc(Long requestId);
}
