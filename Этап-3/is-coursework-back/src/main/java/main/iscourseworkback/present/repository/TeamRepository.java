package main.iscourseworkback.present.repository;

import main.iscourseworkback.present.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Optional<Team> findById(Integer id);
    Team findByName(String name);
}
