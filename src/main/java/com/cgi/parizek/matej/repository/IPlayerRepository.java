
package com.cgi.parizek.matej.repository;

import com.cgi.parizek.matej.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPlayerRepository extends JpaRepository<Player, Long> {

    @Query("SELECT p FROM Player p " +
           "LEFT JOIN FETCH p.profile " +
           "WHERE p.id = :playerId")
    Optional<Player> findByIdWithMissionsAndProfile(@Param("playerId") Long playerId);
}
