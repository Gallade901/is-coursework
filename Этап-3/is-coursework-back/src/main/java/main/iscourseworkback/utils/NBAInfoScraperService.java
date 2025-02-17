package main.iscourseworkback.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import main.iscourseworkback.present.entity.StatTeamSeason;
import main.iscourseworkback.schedule.Game;
import main.iscourseworkback.schedule.NbaResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class NBAInfoScraperService {
    @Value("${base.domain}")
    private String baseDomain;
    private static final String NBA_API_URL = "https://cdn.nba.com/static/json/staticData/scheduleLeagueV2_34.json";
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public NBAInfoScraperService(ObjectMapper objectMapper) {
        this.restClient = RestClient.create();
        this.objectMapper = objectMapper;
    }

    public List<Game> fetchNbaSchedule() {
        try {
            String jsonResponse = restClient.get()
                    .uri(NBA_API_URL)
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

    public List<String> fetchLinksForStat() {

        List<String> linksForStatTeam = new ArrayList<>();
        try {
            String NBA_TEAMS_URL = "https://www.nba.com/teams"; //?cal=all&pd=false&region=34
            Document doc = Jsoup.connect(NBA_TEAMS_URL)
                    .header("User-Agent", "Mozilla/5.0")
                    .timeout(10000)
                    .get();
            Elements teamFigures = doc.select(".TeamFigure_tf__jA5HW");
            for (Element figure : teamFigures) {
                Element statsLink = figure.selectFirst(".TeamFigure_tfLinks__gwWFj a[href*=/stats/team/]");
                if (statsLink != null) {
                    String url = statsLink.attr("href");
                    linksForStatTeam.add(baseDomain + url);
                }
            }
            return linksForStatTeam;
        } catch (IOException e) {
            log.error("Ошибка при получении данных с NBA.com: ", e);
            throw new RuntimeException("Не удалось получить расписание игр", e);
        }
    }

    public List<StatTeamSeason> fetchStatTeamsSeason() {
        List<StatTeamSeason> stats = new ArrayList<>();
        List<String> links = fetchLinksForStat();
        try {
            for (String link : links) {
                link = link + "/traditional";
                Document doc = Jsoup.connect(link)
                        .header("User-Agent", "Mozilla/5.0")
                        .timeout(10000)
                        .get();
                System.out.println(doc);
                Element statSeasonRow = doc.selectFirst("table.Crom_table__p1iZz tbody.Crom_body__UYOcU tr");
                System.out.println(statSeasonRow);
                StatTeamSeason statTeamSeason = new StatTeamSeason();
                if (statSeasonRow != null) {
                    Elements cells = statSeasonRow.select("td");
                    statTeamSeason.setGp(Float.parseFloat(cells.get(1).text()));
                    statTeamSeason.setPts(Float.parseFloat(cells.get(3).text()));
                    statTeamSeason.setWin(Float.parseFloat(cells.get(4).text()));
                    statTeamSeason.setFt(Float.parseFloat(cells.get(15).text()));
                    statTeamSeason.setFreePoints(Float.parseFloat(
                            cells.get(14).selectFirst("a").text()
                    ));
                    statTeamSeason.setReb(Float.parseFloat(
                            cells.get(21).selectFirst("a").text()
                    ));
                    statTeamSeason.setAst(Float.parseFloat(
                            cells.get(24).selectFirst("a").text()
                    ));
                    statTeamSeason.setTov(Float.parseFloat(
                            cells.get(25).selectFirst("a").text()
                    ));
                    statTeamSeason.setStl(Float.parseFloat(
                            cells.get(26).selectFirst("a").text()
                    ));
                    statTeamSeason.setBlk(Float.parseFloat(
                            cells.get(27).selectFirst("a").text()
                    ));

                }
                stats.add(statTeamSeason);
            }
            return stats;
        } catch (IOException e) {
            System.err.println("Ошибка при получении данных: " + e.getMessage());
            return Collections.emptyList();
        }
    }

}
