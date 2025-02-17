package main.iscourseworkback.utils;

import jakarta.annotation.PostConstruct;
import main.iscourseworkback.present.dto.MatchDto;
import main.iscourseworkback.present.entity.StatTeamSeason;
import main.iscourseworkback.schedule.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NBADataLoader {

    @Autowired
    private NBAInfoScraperService nbaInfoScraperService;

    @PostConstruct
    public void loadData() {
        try {
//            List<String> links = nbaInfoScraperService.fetchLinksForStat();
//            List<Game> games = nbaInfoScraperService.fetchNbaSchedule();
            List<StatTeamSeason> stat = nbaInfoScraperService.fetchStatTeamsSeason();
            for (StatTeamSeason statTeamSeason : stat) {
                System.out.println(statTeamSeason.toString());
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}