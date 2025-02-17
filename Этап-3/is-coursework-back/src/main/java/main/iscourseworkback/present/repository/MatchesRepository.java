package main.iscourseworkback.present.repository;

import main.iscourseworkback.present.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchesRepository extends JpaRepository<Match, Integer> {
//    Optional<Match> findByName(String name);
//
//    boolean existsByName(String name);
}