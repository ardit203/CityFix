package finki.ukim.backend.auth_and_access.repository;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.projection.UserPageableProjection;
import finki.ukim.backend.auth_and_access.model.projection.UserWithIdUsernameAndEmail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {
            "profile",
    })
    @Query("select u from User u where u.id = :id")
    Optional<User> findByIdWithProfile(@Param("id") Long id);

    @EntityGraph(attributePaths = {
            "profile",
            "profile.profilePicture"
    })
    @Query("select u from User u where u.id = :id")
    Optional<User> findByIdWithProfileAndPic(@Param("id") Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<User> findAllByRole(Role role);

    @Query(value = """
            select
                    u.id as id,
                    u.username as username,
                    u.email as email
            from User as u
            """)
    List<UserWithIdUsernameAndEmail> findAllWithIdUsernameAndEmail();

    @Query("""
                select
                    u.id as id,
                    u.username as username,
                    u.role as role,
                    p.name as name,
                    p.surname as surname
                from User u
                left join u.profile p
                where (:id is null or u.id = :id)
                  and (:username is null or lower(u.username) like lower(concat('%', :username, '%')))
                  and (:email is null or lower(u.email) like lower(concat('%', :email, '%')))
                  and (:role is null or u.role = :role)
            """)
    Page<UserPageableProjection> findFiltered(
            @Param("id") Long id,
            @Param("username") String username,
            @Param("email") String email,
            @Param("role") Role role,
            Pageable pageable
    );


}
