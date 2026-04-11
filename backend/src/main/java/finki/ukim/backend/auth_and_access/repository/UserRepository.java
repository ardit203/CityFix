package finki.ukim.backend.auth_and_access.repository;

import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.enums.Role;
import finki.ukim.backend.auth_and_access.model.projection.UserWithIdUsernameAndEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
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
            from users as u
            """, nativeQuery = true)
    Optional<UserWithIdUsernameAndEmail> findAllWithIdUsernameAndEmail();


}
