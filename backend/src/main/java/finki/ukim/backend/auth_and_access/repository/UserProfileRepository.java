package finki.ukim.backend.auth_and_access.repository;

import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @Query("select up from UserProfile up where up.user.id = :userId")
    Optional<UserProfile> findWithLockByUserId(@Param("userId") Long userId);

    @EntityGraph(value = "user-profile-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select up from UserProfile up where up.user.id = :userId")
    Optional<UserProfile> findWithUserByUserId(@Param("userId") Long userId);

    @EntityGraph(value = "user-profile-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select up from UserProfile up")
    List<UserProfile> findAllWithUser();
}
