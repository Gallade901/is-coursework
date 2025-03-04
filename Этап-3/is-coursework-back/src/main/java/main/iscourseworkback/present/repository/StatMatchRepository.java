package main.iscourseworkback.present.repository;

import main.iscourseworkback.future.entity.StatPlayerFuture;
import main.iscourseworkback.present.entity.StatMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatMatchRepository extends JpaRepository<StatMatch, Integer> {
    @Query(value = "SELECT * FROM get_stat_match(:idSt)", nativeQuery = true)
    StatMatch get_stat_match(@Param("idSt") Integer idSt);
}
