
package com.cgi.parizek.matej.repository;

import com.cgi.parizek.matej.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface IPlayerRepository extends JpaRepository<Player, Long> {

    @Query("SELECT p FROM Player p " +
            "LEFT JOIN FETCH p.profile " +
            "WHERE p.id = :playerId")
    Optional<Player> findByIdWithProfile(@Param("playerId") Long playerId);

    @Modifying
    @Query("""
            UPDATE Player p
                SET p.status = :status,
                    p.username = :username,
                    p.updatedAt = :updatedAt
            WHERE p.id = :id
            """)
    int update(
            @Param("id") Long id,
            @Param("status") String status,
            @Param("username") String username,
            @Param("updatedAt") LocalDateTime updatedAt
    );
}
