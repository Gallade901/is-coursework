package main.iscourseworkback.present.repository;

import main.iscourseworkback.present.entity.StatMatch;
import main.iscourseworkback.present.entity.StatTeamSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StatTeamSeasonRepository extends JpaRepository<StatTeamSeason, Integer> {
    Optional<StatTeamSeason> findById(Integer seasonId);
    @Query(value = "SELECT * FROM get_stat_team_season(:idSt)", nativeQuery = true)
    StatTeamSeason get_stat_team_season(@Param("idSt") Integer idSt);
}
