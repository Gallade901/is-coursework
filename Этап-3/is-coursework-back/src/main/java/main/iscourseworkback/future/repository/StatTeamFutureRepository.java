package main.iscourseworkback.future.repository;

import main.iscourseworkback.future.entity.StatTeamFuture;
import main.iscourseworkback.present.entity.StatPlayerMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatTeamFutureRepository extends JpaRepository<StatTeamFuture, Integer> {

    @Query(value = "SELECT * FROM get_stat_match_team_future(:idSt)", nativeQuery = true)
    List<StatTeamFuture> get_stat_match_team_future(@Param("idSt") Integer idSt);
}
