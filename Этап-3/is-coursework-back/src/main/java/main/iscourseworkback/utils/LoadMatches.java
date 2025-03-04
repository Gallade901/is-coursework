package main.iscourseworkback.utils;

import jakarta.persistence.EntityNotFoundException;
import main.iscourseworkback.present.entity.*;
import main.iscourseworkback.present.repository.PlayerRepository;
import main.iscourseworkback.present.repository.TeamRepository;
import main.iscourseworkback.schedule.Game;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class LoadMatches {
    @Autowired
    private NBAInfoScraperService nbaInfoScraperService;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    public List<List<?>> getMatches() {
        List<List<?>> result = new ArrayList<>();
        List<StatMatch> statMatches = new ArrayList<>();
        List<Match> matches = new ArrayList<>();
        List<StatMatchTeam> statMatchTeams = new ArrayList<>();
        List<StatPlayerMatch> statPlayerMatches = new ArrayList<>();
        try {
            String filePath = "C:\\Users\\MSI\\Desktop\\отчеты\\ИС\\Курсач\\Этап-3\\is-coursework-back\\MatchesStat.txt";
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONArray jsonArray = jsonObject.getJSONArray("s");
            List<Game> games = nbaInfoScraperService.fetchNbaSchedule();
            for (int i = 0; i < games.size(); i++) {
                Game gameSchedule = games.get(i);
                Instant instant = Instant.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(gameSchedule.getGameDateUTC()));
                LocalDate gameDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate october22nd = LocalDate.of(2024, Month.OCTOBER, 22);
                LocalDate currentDate = LocalDate.now();
                if (gameDate.isBefore(october22nd) ) {
                    continue;
                }
                StatMatch statMatch = new StatMatch();
                Match match = new Match();
                match.setDate(Date.from(gameDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                JSONObject game = jsonArray.getJSONObject(i);
                String gameId = game.getString("gameId");
                JSONObject postGameCharts = game.getJSONObject("postGameCharts");
                JSONObject awayTeamPost = postGameCharts.getJSONObject("awayTeam");
                JSONObject homeTeamPost = postGameCharts.getJSONObject("homeTeam");
//                if (awayTeamPost.getJSONObject("statistics").getInt("points") == 0) {
//                    System.out.println(i);
//                    System.out.println(gameId);
//                    try {
//                        Thread.sleep(10000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                }
                String nameTeamAway = awayTeamPost.getString("teamName");
                String nameTeamHome = homeTeamPost.getString("teamName");
                if (nameTeamAway.contains("Team") || nameTeamHome.contains("Team") || nameTeamAway.contains("Stars") || nameTeamHome.contains("Stars")) continue;
                statMatch.setId(Integer.parseInt(gameId));
                JSONObject awayStatistic = awayTeamPost.getJSONObject("statistics");
                JSONObject homeStatistic = homeTeamPost.getJSONObject("statistics");
                int runAway = awayStatistic.getInt("biggestScoringRun");
                int runHome = homeStatistic.getInt("biggestScoringRun");
                int longestRun = Math.max(runAway, runHome);
                statMatch.setLongestRun(longestRun);
                statMatch.setTimesTied(awayStatistic.getInt("timesTied"));
                statMatch.setLeadChanges(awayStatistic.getInt("leadChanges"));
                JSONObject awayTeam = game.getJSONObject("awayTeam");
                JSONObject homeTeam = game.getJSONObject("homeTeam");
                int win = awayTeam.getInt("teamWins");
                String winName = win == 1 ? awayTeam.getString("teamName") : homeTeam.getString("teamName");
                statMatch.setWinnersName(winName);

                if (!gameSchedule.getGameId().equals(gameId)) {
                    throw new RuntimeException("id игр не совпадают");
                }
                match.setId(Integer.parseInt(gameId));
                match.setLocation(gameSchedule.getArenaName());
                Optional<Team> team1 = teamRepository.findById(gameSchedule.getAwayTeam().getId());
                match.setTeam1(team1.orElseThrow(() ->
                        new EntityNotFoundException("Away team in schedule not found with id: " + gameSchedule.getAwayTeam().getId())));
                Optional<Team> team2 = teamRepository.findById(gameSchedule.getHomeTeam().getId());
                match.setTeam2(team2.orElseThrow(() ->
                        new EntityNotFoundException("Home team in schedule not found with id: " + gameSchedule.getHomeTeam().getId())));
                if (awayTeamPost.getJSONObject("statistics").getInt("points") == 0) {
                    match.setStatMatch(null);
                    matches.add(match);
                    continue;
                }
                match.setStatMatch(statMatch);
                StatMatchTeam statMatchTeam1 = new StatMatchTeam();
                StatMatchTeam statMatchTeam2 = new StatMatchTeam();
                statMatchTeam1 = setParamStatMatchTeam(statMatchTeam1, awayStatistic);
                statMatchTeam1.setIdTeam(teamRepository.findById(awayTeamPost.getInt("teamId")).orElseThrow(
                        () -> new EntityNotFoundException("Away team not found with id: " + awayTeamPost.getInt("teamId"))));
                statMatchTeam1.setIdStatMatch(statMatch);

                statMatchTeam2 = setParamStatMatchTeam(statMatchTeam2, homeStatistic);
                statMatchTeam2.setIdTeam(teamRepository.findById(homeTeamPost.getInt("teamId")).orElseThrow(
                        () -> new EntityNotFoundException("Home team not found with id: " + homeTeamPost.getInt("teamId"))));
                statMatchTeam2.setIdStatMatch(statMatch);

                JSONArray awayPlayers = awayTeam.getJSONArray("players");
                JSONArray homePlayers = homeTeam.getJSONArray("players");
                matches.add(match);
                if (gameDate.isAfter(currentDate)) continue;
                statMatches.add(statMatch);

                statMatchTeams.add(statMatchTeam1);
                statMatchTeams.add(statMatchTeam2);
                setPlayerStat(awayPlayers, statMatch, statPlayerMatches);
                setPlayerStat(homePlayers, statMatch, statPlayerMatches);
            }
            result.add(statMatches);
            result.add(matches);
            result.add(statMatchTeams);
            result.add(statPlayerMatches);
            return result;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    public StatMatchTeam setParamStatMatchTeam(StatMatchTeam statMatchTeam, JSONObject json) {
        statMatchTeam.setPts(json.getInt("points"));
        statMatchTeam.setFg(json.getFloat("fieldGoalsPercentage"));
        statMatchTeam.setThreePoints(json.getFloat("threePointersPercentage"));
        statMatchTeam.setFt(json.getFloat("freeThrowsPercentage"));
        statMatchTeam.setReb(json.getInt("reboundsTotal"));
        statMatchTeam.setAst(json.getInt("assists"));
        statMatchTeam.setTov(json.getInt("turnovers"));
        statMatchTeam.setStl(json.getInt("steals"));
        statMatchTeam.setBlk(json.getInt("blocks"));
        return statMatchTeam;
    }

    public void setPlayerStat(JSONArray json, StatMatch statMatch, List<StatPlayerMatch> array) {

        for (int i = 0; i < json.length(); i++) {
            JSONObject player = json.getJSONObject(i);
            JSONObject statistics = player.getJSONObject("statistics");
            if (statistics.getString("minutes").isEmpty()) continue;
            StatPlayerMatch statPlayerMatch = new StatPlayerMatch();
            statPlayerMatch.setMin(statistics.getString("minutes"));
            statPlayerMatch.setPts(statistics.getInt("points"));
            statPlayerMatch.setFg(statistics.getFloat("fieldGoalsPercentage"));
            statPlayerMatch.setThreePoints(statistics.getFloat("threePointersPercentage"));
            statPlayerMatch.setFt(statistics.getFloat("freeThrowsPercentage"));
            statPlayerMatch.setReb(statistics.getInt("reboundsTotal"));
            statPlayerMatch.setAst(statistics.getInt("assists"));
            statPlayerMatch.setTov(statistics.getInt("turnovers"));
            statPlayerMatch.setStl(statistics.getInt("steals"));
            statPlayerMatch.setBlk(statistics.getInt("blocks"));
            statPlayerMatch.setIdPlayer(playerRepository.findById(player.getInt("personId"))
                    .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + player.getInt("personId"))));
            statPlayerMatch.setIdStatMatch(statMatch);
            array.add(statPlayerMatch);
        }
    }
}
