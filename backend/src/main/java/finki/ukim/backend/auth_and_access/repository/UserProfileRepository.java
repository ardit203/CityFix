package finki.ukim.backend.auth_and_access.repository;

import finki.ukim.backend.auth_and_access.model.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);

    Optional<UserProfile> findByUser_Username(String userUsername);

    @Query("""
                select up
                from UserProfile up
                join fetch up.user u
                where u.username = :username
            """)
    Optional<UserProfile> findByUsername(@Param("username") String username);

    @Query("""
                select up
                from UserProfile up
                join fetch up.user u
                where u.id = :userId
            """)
    Optional<UserProfile> findByUserIdWithUser(@Param("userId") Long userId);
}
