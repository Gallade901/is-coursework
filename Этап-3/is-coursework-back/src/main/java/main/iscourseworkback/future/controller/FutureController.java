package main.iscourseworkback.future.controller;

import main.iscourseworkback.future.dto.StatPlayersMatchFutureDTO;
import main.iscourseworkback.future.entity.MatchFuture;
import main.iscourseworkback.future.entity.StatMatchFuture;
import main.iscourseworkback.future.entity.StatPlayerFuture;
import main.iscourseworkback.future.entity.StatTeamFuture;
import main.iscourseworkback.future.repository.MatchFutureRepository;
import main.iscourseworkback.future.repository.StatMatchFutureRepository;
import main.iscourseworkback.future.repository.StatPlayerFutureRepository;
import main.iscourseworkback.future.repository.StatTeamFutureRepository;
import main.iscourseworkback.present.dto.StatPlayersMatchDTO;
import main.iscourseworkback.present.entity.*;
import main.iscourseworkback.present.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/predict")
public class FutureController {
    @Autowired
    private StatMatchFutureRepository statMatchFutureRepository;
    @Autowired
    private StatTeamFutureRepository statTeamFutureRepository;
    @Autowired
    private MatchFutureRepository matchFutureRepository;
    @Autowired
    private StatPlayerFutureRepository statPlayerFutureRepository;

    @GetMapping("/getStatMatchFuture/{id}")
    public ResponseEntity<StatMatchFuture> getStatMatchFuture(@PathVariable int id) {
        StatMatchFuture statMatchFuture = statMatchFutureRepository.findById(id).get();
        return ResponseEntity.ok(statMatchFuture);
    }

    @PostMapping("/getStatPlayersMatchFuture")
    public ResponseEntity<StatPlayersMatchFutureDTO> getStatPlayersMatchFuture(@RequestBody int id) {
        MatchFuture match = matchFutureRepository.findById(id).get();
        Team team1 = match.getTeam1();
        Team team2 = match.getTeam2();
        int team1Id = team1.getId();
        int team2Id = team2.getId();
        List<StatPlayerFuture> statPlayersMatch = statPlayerFutureRepository.get_stat_match_player_future(id);
        List<StatPlayerFuture> statPlayersTeam1 = new ArrayList<>();
        List<StatPlayerFuture> statPlayersTeam2 = new ArrayList<>();
        for (StatPlayerFuture stat : statPlayersMatch) {
            if (stat.getIdPlayer().getIdTeam().getId() == team1Id) {
                statPlayersTeam1.add(stat);
            } else {
                statPlayersTeam2.add(stat);
            }
        }
        return ResponseEntity.ok(new StatPlayersMatchFutureDTO(statPlayersTeam1, statPlayersTeam2, team1.getName(), team2.getName(), team1Id, team2Id));
    }

    @PostMapping("/getStatTeamsMatchFuture")
    public ResponseEntity<List<StatTeamFuture>> getStatTeamsMatchFuture(@RequestBody int id) {
        List<StatTeamFuture> statMatchTeams = statTeamFutureRepository.get_stat_match_team_future(id);
        return ResponseEntity.ok(statMatchTeams);
    }
}
