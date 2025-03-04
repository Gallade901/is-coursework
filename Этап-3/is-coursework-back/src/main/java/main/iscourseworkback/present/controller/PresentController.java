package main.iscourseworkback.present.controller;

import lombok.RequiredArgsConstructor;
import main.iscourseworkback.present.dto.StatPlayersMatchDTO;
import main.iscourseworkback.present.dto.TeamStatDTO;
import main.iscourseworkback.present.entity.*;
import main.iscourseworkback.present.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/main")
public class PresentController {
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private StatMatchRepository statMatchRepository;
    @Autowired
    private StatMatchTeamRepository statMatchTeamRepository;
    @Autowired
    private StatPlayerMatchRepository statPlayerMatchRepository;
    @Autowired
    private StatPlayerRepository statPlayerRepository;
    @Autowired
    private StatTeamSeasonRepository statTeamSeasonRepository;
    @Autowired
    private TeamRepository teamRepository;


    @GetMapping("/")
    public ResponseEntity<String> test () {
        return ResponseEntity.ok("Welcome");
    }

    @GetMapping("/getMatches")
    public ResponseEntity<List<Match>> getMatch () {
        List<Match> matches = matchRepository.findAll();
        return ResponseEntity.ok(matches);
    }
    @GetMapping("/getStatMatches/{id}")
    public ResponseEntity<StatMatch> getStatMatches(@PathVariable int id) {
        StatMatch statMatch = statMatchRepository.get_stat_match(id);
        return ResponseEntity.ok(statMatch);
    }
    @PostMapping("/getStatPlayersMatch")
    public ResponseEntity<StatPlayersMatchDTO> getStatPlayersMatch(@RequestBody int id) {
        Match match = matchRepository.findById(id).get();
        Team team1 = match.getTeam1();
        Team team2 = match.getTeam2();
        int team1Id = team1.getId();
        int team2Id = team2.getId();
        List<StatPlayerMatch> statPlayersMatch = statPlayerMatchRepository.get_stat_match_player(id);
        List<StatPlayerMatch> statPlayersTeam1 = new ArrayList<>();
        List<StatPlayerMatch> statPlayersTeam2 = new ArrayList<>();
        for (StatPlayerMatch stat : statPlayersMatch) {
            if (stat.getIdPlayer().getIdTeam().getId() == team1Id) {
                statPlayersTeam1.add(stat);
            } else {
                statPlayersTeam2.add(stat);
            }
        }
        return ResponseEntity.ok(new StatPlayersMatchDTO(statPlayersTeam1, statPlayersTeam2, team1.getName(), team2.getName(), team1Id, team2Id));
    }
    @PostMapping("/getStatTeamsMatch")
    public ResponseEntity<List<StatMatchTeam>> getStatTeamsMatch(@RequestBody int id) {
        List<StatMatchTeam> statMatchTeams = statMatchTeamRepository.get_stat_match_team(id);
        return ResponseEntity.ok(statMatchTeams);
    }
    @GetMapping("/getStatPlayer/{id}")
    public ResponseEntity<StatPlayer> getStatPlayer(@PathVariable int id) {
        StatPlayer statPlayer = statPlayerRepository.get_stat_player(id);
        return ResponseEntity.ok(statPlayer);
    }
    @GetMapping("/checkMatch/{id}")
    public ResponseEntity<Boolean> checkMatch(@PathVariable int id) {
        Match match = matchRepository.findById(id).get();
        boolean ans = match.getStatMatch() != null;
        return ResponseEntity.ok(ans);
    }
    @GetMapping("/getStatTeam/{id}")
    public ResponseEntity<TeamStatDTO> getStatTeam(@PathVariable int id) {
        Team team = teamRepository.findById(id).get();
        StatTeamSeason statTeamSeason = statTeamSeasonRepository.get_stat_team_season(id);
        return ResponseEntity.ok(new TeamStatDTO(statTeamSeason, team.getName()));
    }
}
