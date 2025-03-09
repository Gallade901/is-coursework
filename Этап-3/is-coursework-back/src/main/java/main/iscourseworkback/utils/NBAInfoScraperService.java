package main.iscourseworkback.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import main.iscourseworkback.present.entity.*;
import main.iscourseworkback.present.repository.TeamRepository;
import main.iscourseworkback.schedule.Game;
import main.iscourseworkback.schedule.NbaResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NBAInfoScraperService {
    @Value("${base.domain}")
    private String baseDomain;
    @Autowired
    private TeamRepository teamRepository;
    private static final String NBA_API_URL_SCHEDULE = "https://cdn.nba.com/static/json/staticData/scheduleLeagueV2_34.json";
    private static final String NBA_API_URL_STAT_TEAM_SEASON = "https://stats.nba.com/stats/leaguedashteamstats"; //?Conference=&DateFrom=&DateTo=&Division=&GameScope=&GameSegment=&Height=&ISTRound=&LastNGames=0&LeagueID=00&Location=&MeasureType=Base&Month=0&OpponentTeamID=0&Outcome=&PORound=0&PaceAdjust=N&PerMode=PerGame&Period=0&PlayerExperience=&PlayerPosition=&PlusMinus=N&Rank=N&Season=2024-25&SeasonSegment=&SeasonType=Regular%20Season&ShotClockRange=&StarterBench=&TeamID=0&TwoWay=0&VsConference=&VsDivision=";
    private static final String NBA_API_URL_STAT_PLAYER_SEASON = "https://stats.nba.com/stats/leaguedashplayerstats";
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public NBAInfoScraperService(ObjectMapper objectMapper) {
        this.restClient = RestClient.create();
        this.objectMapper = objectMapper;
    }

    public List<Game> fetchNbaSchedule() {
        try {
            String jsonResponse = restClient.get()
                    .uri(NBA_API_URL_SCHEDULE)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .body(String.class);
            NbaResponse nbaResponse = objectMapper.readValue(jsonResponse, NbaResponse.class);
            List<Game> games = new ArrayList<>();
            nbaResponse.getLeagueSchedule().getGameDates().forEach(gameDate -> {
                games.addAll(gameDate.getGames());
            });
            return games;
        } catch (Exception e) {
            log.error("Ошибка при получении данных: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }


    public List<List<?>> fetchStatTeamsSeason() {
        List<StatTeamSeason> stats = new ArrayList<>();
        List<Team> teams = new ArrayList<>();
        try {
            UriComponents uriComponents = getUriComponent(NBA_API_URL_STAT_TEAM_SEASON);
            String jsonResponse = restClient.get()
                    .uri(uriComponents.toUri())
                    .header( "Referer", "https://www.nba.com")
                    .header("Host", "stats.nba.com")
                    .header("Accept", "*/*")
                    .retrieve()
                    .body(String.class);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            String jsonString = jsonObject.toString();
            JSONArray resultSets = jsonObject.getJSONArray("resultSets");
            JSONObject first = resultSets.getJSONObject(0);
            JSONArray rowSet = first.getJSONArray("rowSet");
            for (int i = 0; i < rowSet.length(); i++) {
                StatTeamSeason statTeamSeason = new StatTeamSeason();
                Team team = new Team();
                JSONArray teamStat = rowSet.getJSONArray(i);
                statTeamSeason.setId(teamStat.getInt(0));
                team.setId(teamStat.getInt(0));
                team.setName(teamStat.getString(1));
                team.setIdStat(statTeamSeason);
                statTeamSeason.setGp(teamStat.getInt(2));
                statTeamSeason.setWin(teamStat.getFloat(5));
                statTeamSeason.setPts(teamStat.getFloat(26));
                statTeamSeason.setFg(teamStat.getFloat(9));
                statTeamSeason.setThreePoints(teamStat.getFloat(12));
                statTeamSeason.setFt(teamStat.getFloat(15));
                statTeamSeason.setReb(teamStat.getFloat(18));
                statTeamSeason.setAst(teamStat.getFloat(19));
                statTeamSeason.setTov(teamStat.getFloat(20));
                statTeamSeason.setStl(teamStat.getFloat(21));
                statTeamSeason.setBlk(teamStat.getFloat(22));
                stats.add(statTeamSeason);
                teams.add(team);
            }
            List<List<?>> lists = new ArrayList<>();
            lists.add(stats);
            lists.add(teams);
            return lists;
        } catch (Exception e) {
            log.error("Ошибка при получении данных: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<List<?>> fetchStatPlayerSeason() {
        List<StatPlayer> stats = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        try {
            UriComponents uriComponents = getUriComponent(NBA_API_URL_STAT_PLAYER_SEASON);
            String jsonResponse = restClient.get()
                    .uri(uriComponents.toUri())
                    .header( "Referer", "https://www.nba.com")
                    .header("Host", "stats.nba.com")
                    .header("Accept", "*/*")
                    .retrieve()
                    .body(String.class);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            String jsonString = jsonObject.toString();
            JSONArray resultSets = jsonObject.getJSONArray("resultSets");
            JSONObject first = resultSets.getJSONObject(0);
            JSONArray rowSet = first.getJSONArray("rowSet");
            for (int i = 0; i < rowSet.length(); i++) {
                StatPlayer statPlayer = new StatPlayer();
                Player player = new Player();
                JSONArray playerStat = rowSet.getJSONArray(i);
                statPlayer.setId(playerStat.getInt(0));
                player.setId(playerStat.getInt(0));
                player.setName(playerStat.getString(1));
                int team_id = playerStat.getInt(3);
                Optional<Team> team2 = teamRepository.findById(team_id);
                player.setIdTeam(team2.orElseThrow(() ->
                        new EntityNotFoundException("Team not found with id: " + team_id)));

                statPlayer.setGp(playerStat.getFloat(6));
                statPlayer.setMin(playerStat.getFloat(10));
                statPlayer.setPts(playerStat.getFloat(30));
                statPlayer.setFg(playerStat.getFloat(13));
                statPlayer.setThreePoints(playerStat.getFloat(16));
                statPlayer.setFt(playerStat.getFloat(19));
                statPlayer.setReb(playerStat.getFloat(22));
                statPlayer.setAst(playerStat.getFloat(23));
                statPlayer.setTov(playerStat.getFloat(24));
                statPlayer.setStl(playerStat.getFloat(25));
                statPlayer.setBlk(playerStat.getFloat(26));
                stats.add(statPlayer);
                players.add(player);
            }
            List<List<?>> lists = new ArrayList<>();
            lists.add(stats);
            lists.add(players);
            return lists;
        } catch (Exception e) {
            log.error("Ошибка при получении данных: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public UriComponents getUriComponent(String url) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("Conference", "")
                .queryParam("DateFrom", "")
                .queryParam("DateTo", "")
                .queryParam("Division", "")
                .queryParam("GameScope", "")
                .queryParam("GameSegment", "")
                .queryParam("Height", "")
                .queryParam("ISTRound", "")
                .queryParam("LastNGames", "0")
                .queryParam("LeagueID", "00")
                .queryParam("Location", "")
                .queryParam("MeasureType", "Base")
                .queryParam("Month", "0")
                .queryParam("OpponentTeamID", "0")
                .queryParam("Outcome", "")
                .queryParam("PORound", "0")
                .queryParam("PaceAdjust", "N")
                .queryParam("PerMode", "PerGame")
                .queryParam("Period", "0")
                .queryParam("PlayerExperience", "")
                .queryParam("PlayerPosition", "")
                .queryParam("PlusMinus", "N")
                .queryParam("Rank", "N")
                .queryParam("Season", "2024-25")
                .queryParam("SeasonSegment", "")
                .queryParam("SeasonType", "Regular Season")
                .queryParam("ShotClockRange", "")
                .queryParam("StarterBench", "")
                .queryParam("TeamID", "0")
                .queryParam("TwoWay", "0")
                .queryParam("VsConference", "")
                .queryParam("VsDivision", "")
                .encode()
                .build();
        return uriComponents;
    }

    public String fetchStatMatch() { //String
//        String filePath = "MatchesStat.txt";
//        String jsonContent = null;
//        try {
//            jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
//            JSONObject jsonObject = new JSONObject(jsonContent);
        String filePath = "/MatchesStat.txt"; // Указываем путь относительно папки resources
        String jsonContent = null;

        try (InputStream inputStream = getClass().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Файл не найден: " + filePath);
            }

            jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(jsonContent);
            //
            JSONArray jsonArray = jsonObject.getJSONArray("s");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject game = jsonArray.getJSONObject(i);
                if (game.getJSONObject("postGameCharts").getJSONObject("awayTeam").getJSONObject("statistics").getInt("points") == 0) {
                    String id = game.getString("gameId");
                    Document doc = Jsoup.connect("https://www.nba.com/game/" + id + "/box-score").get();
                    Element scriptTag = doc.getElementById("__NEXT_DATA__");
                    if (scriptTag == null) {
                        throw new RuntimeException("Script тег не найден");
                    }
                    JSONObject obj = new JSONObject(scriptTag.data());
                    JSONObject gameObj = obj.getJSONObject("props").getJSONObject("pageProps").getJSONObject("game");
                    JSONObject homeTeam = gameObj.getJSONObject("homeTeam");
                    JSONObject awayTeam = gameObj.getJSONObject("awayTeam");
                    JSONObject postGameCharts = gameObj.getJSONObject("postgameCharts");
                    if (postGameCharts.getJSONObject("awayTeam").getJSONObject("statistics").getInt("playerPtsLeaderId") == 0) {
                        break;
                    }
                    JSONObject result = new JSONObject();
                    result.put("gameId", id);
                    result.put("homeTeam", homeTeam);
                    result.put("awayTeam", awayTeam);
                    result.put("postGameCharts", postGameCharts);
                    jsonArray.put(i, result);
                    System.out.println(i);
                }
            }
//            Files.writeString(Paths.get(filePath), jsonObject.toString(4));
            Path tempFile = Files.createTempFile("MatchesStat", ".txt");
            Files.writeString(tempFile, jsonObject.toString(4), StandardCharsets.UTF_8);
            System.out.println("Файл записан во временный путь: " + tempFile.toAbsolutePath());
            return tempFile.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
