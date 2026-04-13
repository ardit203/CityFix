package finki.ukim.backend.auth_and_access.repository;

import finki.ukim.backend.auth_and_access.model.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    List<PasswordResetToken> findAllByUser_Id(Long userId);

    @EntityGraph(value = "password-reset-token-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    List<PasswordResetToken> findByUserId(Long userId);

    @Query("""
            select pt from PasswordResetToken pt
            where pt.user.id = :userId
              and pt.expiresAt > CURRENT_TIMESTAMP
              and pt.usedAt is null
              and pt.invalidatedAt is null
           """)
    Optional<PasswordResetToken> findActiveByUserId(@Param("userId") Long userId);

    @Query("""
            select pt from PasswordResetToken pt
            where pt.expiresAt < CURRENT_TIMESTAMP
               or pt.usedAt is not null
               or pt.invalidatedAt is not null
           """)
    List<PasswordResetToken> findAllInactiveTokens();
}
