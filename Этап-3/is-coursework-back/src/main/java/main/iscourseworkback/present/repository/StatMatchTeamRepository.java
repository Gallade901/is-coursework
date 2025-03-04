package main.iscourseworkback.present.repository;

import main.iscourseworkback.present.entity.StatMatch;
import main.iscourseworkback.present.entity.StatMatchTeam;
import main.iscourseworkback.present.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatMatchTeamRepository extends JpaRepository<StatMatchTeam, Integer> {
    @Query("SELECT DISTINCT smt FROM StatMatchTeam smt WHERE smt.idStatMatch = :statMatch AND smt.idTeam = :team")
    StatMatchTeam findByIdStatMatchAndIdTeam(@Param("statMatch") StatMatch statMatch, @Param("team") Team team);

    @Query(value = "SELECT * FROM get_stat_match_team(:idSt)", nativeQuery = true)
    List<StatMatchTeam> get_stat_match_team(@Param("idSt") Integer idSt);

}
