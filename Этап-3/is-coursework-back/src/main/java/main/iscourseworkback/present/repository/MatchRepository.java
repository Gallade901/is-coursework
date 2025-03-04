package main.iscourseworkback.present.repository;

import main.iscourseworkback.present.entity.Match;
import main.iscourseworkback.present.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {

    @Query("SELECT m FROM Match m WHERE (m.team1 = :team1 AND m.team2 = :team2) AND m.date < :targetDate ORDER BY m.date DESC LIMIT 1")
    Match findByTeam1AndTeam2(@Param("team1") Team team1, @Param("team2") Team team2, @Param("targetDate") LocalDateTime targetDate);
    @Query("SELECT m FROM Match m WHERE (m.team1 = :team1) AND m.date < :targetDate ORDER BY m.date DESC LIMIT 1")
    Match findByTeam1(@Param("team1") Team team1,  @Param("targetDate") LocalDateTime targetDate);
    @Query("SELECT m FROM Match m WHERE (m.team2 = :team2) AND m.date < :targetDate ORDER BY m.date DESC LIMIT 1")
    Match findByTeam2(@Param("team2") Team team2, @Param("targetDate") LocalDateTime targetDate);
    @Query("SELECT m FROM Match m WHERE (m.team1 = :team OR m.team2 = :team) AND m.date < :targetDate ORDER BY m.date DESC LIMIT 1")
    Optional<Match> findLatestMatchByTeam(@Param("team") Team team, @Param("targetDate") LocalDateTime targetDate);
}
