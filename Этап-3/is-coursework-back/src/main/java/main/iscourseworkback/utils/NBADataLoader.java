package main.iscourseworkback.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.iscourseworkback.future.entity.MatchFuture;
import main.iscourseworkback.future.entity.StatMatchFuture;
import main.iscourseworkback.future.entity.StatPlayerFuture;
import main.iscourseworkback.future.entity.StatTeamFuture;
import main.iscourseworkback.future.repository.MatchFutureRepository;
import main.iscourseworkback.future.repository.StatMatchFutureRepository;
import main.iscourseworkback.future.repository.StatPlayerFutureRepository;
import main.iscourseworkback.future.repository.StatTeamFutureRepository;
import main.iscourseworkback.present.entity.*;
import main.iscourseworkback.present.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NBADataLoader {
    @Autowired
    private NBAInfoScraperService nbaInfoScraperService;
    @Autowired
    private LoadMatches loadMatches;
    @Autowired
    private AnalysisStat analysisStat;
    private final StatTeamSeasonRepository statTeamSeasonRepository;
    private final TeamRepository teamRepository;
    private final StatPlayerRepository statPlayerRepository;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final StatMatchRepository statMatchRepository;
    private final StatMatchTeamRepository statMatchTeamRepository;
    private final StatPlayerMatchRepository statPlayerMatchRepository;
    private final StatMatchFutureRepository statMatchFutureRepository;
    private final MatchFutureRepository matchFutureRepository;
    @Autowired
    private StatTeamFutureRepository statTeamFutureRepository;
    @Autowired
    private StatPlayerFutureRepository statPlayerFutureRepository;

    @PostConstruct
    public void loadData() {
        try {
            String path = nbaInfoScraperService.fetchStatMatch();
            log.info("fetchMatch");
            List<?> stat = nbaInfoScraperService.fetchStatTeamsSeason();
            log.info("fetchStatTeamsSeason");
            List<StatTeamSeason> statTeamSeasons = (List<StatTeamSeason>) stat.get(0);
            List<Team> teams = (List<Team>) stat.get(1);
            for (int i = 0; i < teams.size(); i++) {
                Team team = teams.get(i);
                StatTeamSeason statTeamSeason = statTeamSeasons.get(i);
                team.setIdStat(statTeamSeason);
                statTeamSeasonRepository.save(statTeamSeason);
                team.setIdStat(statTeamSeason);
                teamRepository.save(team);
            }
            log.info("Save Stat Team");
            List<?> statP = nbaInfoScraperService.fetchStatPlayerSeason();
            log.info("fetchStatPlayerSeason");
            List<StatPlayer> statPlayersSeasons = (List<StatPlayer>) statP.get(0);
            List<Player> players = (List<Player>) statP.get(1);
            for (int i = 0; i < statPlayersSeasons.size(); i++) {
                StatPlayer statPlayer = statPlayersSeasons.get(i);
                Player player = players.get(i);
                playerRepository.save(player);
                statPlayer.setPlayer(player);
                statPlayerRepository.save(statPlayer);
            }
            log.info("Save Stat Player");
            List<List<?>> listMatches = loadMatches.getMatches(path);
            List<StatMatch> statMatches = (List<StatMatch>) listMatches.get(0);
            List<Match> matches = (List<Match>) listMatches.get(1);
            List<StatMatchTeam> statMatchTeams = (List<StatMatchTeam>) listMatches.get(2);
            List<StatPlayerMatch> statPlayerMatches = (List<StatPlayerMatch>) listMatches.get(3);
            log.info("Get Matches");
            statMatchRepository.saveAll(statMatches);
            matchRepository.saveAll(matches);
            statMatchTeamRepository.saveAll(statMatchTeams);
            statPlayerMatchRepository.saveAll(statPlayerMatches);
            log.info("Save matches");
            for (int i = 0; i < matches.size(); i++) {
                Match match = matches.get(i);
                Date dateObject = match.getDate();
                LocalDate localDate = dateObject.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (localDate.isAfter(LocalDate.now())) {
                    MatchFuture matchFuture = new MatchFuture();
                    matchFuture.setDate(match.getDate());
                    matchFuture.setLocation(match.getLocation());
                    matchFuture.setId(match.getId());
                    matchFuture.setTeam1(match.getTeam1());
                    matchFuture.setTeam2(match.getTeam2());
                    String teamName1 = match.getTeam1().getName();
                    String teamName2 = match.getTeam2().getName();
                    log.info("Before Analysis match");
                    StatMatchFuture statMatchFuture = analysisStat.analyzeStatMatch(teamName1, teamName2);
                    statMatchFuture.setId(match.getId());
                    matchFuture.setStatMatch(statMatchFuture);
                    statMatchFutureRepository.save(statMatchFuture);
                    matchFutureRepository.save(matchFuture);
                    log.info("Before Analysis two team");
                    StatTeamFuture statTeamFuture1 = analysisStat.analyzeStatTeam(teamName1, teamName2);
                    StatTeamFuture statTeamFuture2 = analysisStat.analyzeStatTeam(teamName1, teamName2);
                    statTeamFuture1.setIdStatMatch(statMatchFuture);
                    statTeamFuture2.setIdStatMatch(statMatchFuture);
                    statTeamFuture1.setIdTeam(match.getTeam1());
                    statTeamFuture2.setIdTeam(match.getTeam2());
                    log.info("Analysis two team");
                    statTeamFutureRepository.save(statTeamFuture1);
                    statTeamFutureRepository.save(statTeamFuture2);
                    List<StatPlayerFuture> listPlayers = analysisStat.analyzeStatPlayerMatch(teamName1, teamName2);
                    log.info("Analysis statPlayer");
                    for (StatPlayerFuture statPlayerFuture : listPlayers) {
                        statPlayerFuture.setIdStatMatch(statMatchFuture);
                        statPlayerFutureRepository.save(statPlayerFuture);
                    }
                }
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}