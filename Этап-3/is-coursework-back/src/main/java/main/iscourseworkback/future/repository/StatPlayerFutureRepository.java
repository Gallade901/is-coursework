package main.iscourseworkback.future.repository;

import main.iscourseworkback.future.entity.StatPlayerFuture;
import main.iscourseworkback.future.entity.StatTeamFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatPlayerFutureRepository extends JpaRepository<StatPlayerFuture, Integer> {
    @Query(value = "SELECT * FROM get_stat_match_player_future(:idSt)", nativeQuery = true)
    List<StatPlayerFuture> get_stat_match_player_future(@Param("idSt") Integer idSt);
}
