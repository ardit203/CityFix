package finki.ukim.backend.auth_and_access.repository;

import finki.ukim.backend.auth_and_access.model.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("""
            select pt from PasswordResetToken pt
            join fetch pt.user
            where pt.tokenHash = :tokenHash
            """)
    Optional<PasswordResetToken> findByTokenHashWithUser(@Param("tokenHash") String tokenHash);


    /// ////

    @Query("""
            select pt from PasswordResetToken pt
            where pt.user.id = :userId
              and pt.expiresAt > CURRENT_TIMESTAMP
              and pt.usedAt is null
              and pt.invalidatedAt is null
            """)
    List<PasswordResetToken> findAllActiveByUserId(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update PasswordResetToken pt
            set pt.invalidatedAt = CURRENT_TIMESTAMP
            where pt.user.id = :userId
              and pt.expiresAt > CURRENT_TIMESTAMP
              and pt.usedAt is null
              and pt.invalidatedAt is null
            """)
    void invalidateAllActiveByUserId(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update PasswordResetToken pt
            set pt.invalidatedAt = CURRENT_TIMESTAMP
            where pt.user.id = :userId
              and pt.id <> :currentTokenId
              and pt.expiresAt > CURRENT_TIMESTAMP
              and pt.usedAt is null
              and pt.invalidatedAt is null
            """)
    void invalidateOtherActiveByUserId(@Param("userId") Long userId, @Param("currentTokenId") Long currentTokenId);
}
