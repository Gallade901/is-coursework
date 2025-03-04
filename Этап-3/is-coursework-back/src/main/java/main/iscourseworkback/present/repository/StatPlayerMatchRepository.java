package main.iscourseworkback.present.repository;

import main.iscourseworkback.present.entity.StatMatch;
import main.iscourseworkback.present.entity.StatPlayerMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatPlayerMatchRepository extends JpaRepository<StatPlayerMatch, Integer> {
    List<StatPlayerMatch> findByIdStatMatch(StatMatch statMatch);
    @Query(value = "SELECT * FROM get_stat_match_player(:idSt)", nativeQuery = true)
    List<StatPlayerMatch> get_stat_match_player(@Param("idSt") Integer idSt);
}
