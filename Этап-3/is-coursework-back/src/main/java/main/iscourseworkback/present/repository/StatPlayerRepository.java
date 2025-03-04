package main.iscourseworkback.present.repository;

import main.iscourseworkback.present.entity.StatMatch;
import main.iscourseworkback.present.entity.StatPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatPlayerRepository extends JpaRepository<StatPlayer, Integer> {
    @Query(value = "SELECT * FROM get_stat_player(:idSt)", nativeQuery = true)
    StatPlayer get_stat_player(@Param("idSt") Integer idSt);
}
