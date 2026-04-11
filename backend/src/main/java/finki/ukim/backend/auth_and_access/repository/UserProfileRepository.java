package finki.ukim.backend.auth_and_access.repository;

import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findWithLockByUserId(Long userId);

    @EntityGraph(value = "user-profile-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserProfile> findWithUserByUserId(Long userId);

    @EntityGraph(value = "user-profile-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    List<UserProfile> findAllWithUser();
}
