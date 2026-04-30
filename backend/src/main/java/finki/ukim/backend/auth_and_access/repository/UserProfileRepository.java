package finki.ukim.backend.auth_and_access.repository;

import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @EntityGraph(attributePaths = {"profilePicture"})
    Optional<UserProfile> findByUser_Username(String userUsername);

    @EntityGraph(attributePaths = {"profilePicture"})
    Optional<UserProfile> findByUserId(Long userId);


    @Query("""
            select up from UserProfile up
            join fetch up.user
            join fetch up.profilePicture
            where up.userId = :userId
            """)
    Optional<UserProfile> findByUserIdWithUserAndProfilePic(@Param("userId") Long userId);
}
