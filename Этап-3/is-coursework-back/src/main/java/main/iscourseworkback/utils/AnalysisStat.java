package main.iscourseworkback.utils;

import jakarta.persistence.EntityNotFoundException;
import main.iscourseworkback.future.entity.StatMatchFuture;
import main.iscourseworkback.future.entity.StatPlayerFuture;
import main.iscourseworkback.future.entity.StatTeamFuture;
import main.iscourseworkback.present.entity.*;
import main.iscourseworkback.present.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AnalysisStat {
    @Autowired
    private StatMatchRepository statMatchRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private StatMatchTeamRepository statMatchTeamRepository;
    @Autowired
    private StatPlayerRepository statPlayerRepository;
    @Autowired
    private StatTeamSeasonRepository statTeamSeasonRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private StatPlayerMatchRepository statPlayerMatchRepository;

    public StatMatchFuture analyzeStatMatch(String teamName1, String teamName2) {
        Team team1 = teamRepository.findByName(teamName1);
        Team team2 = teamRepository.findByName(teamName2);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fourHoursAgo = now.minusHours(48);
        Match twoTeamMatch = matchRepository.findByTeam1AndTeam2(team1, team2, fourHoursAgo);
        Match team1Match = matchRepository.findByTeam1(team1, fourHoursAgo);
        Match team2Match = matchRepository.findByTeam2(team2, fourHoursAgo);
        System.out.println(team1Match);
        System.out.println(team2Match);
        StatMatch statMatch1 = statMatchRepository.findById(team1Match.getStatMatch().getId()).get();
        System.out.println(statMatch1);
        StatMatch statMatch2 = statMatchRepository.findById(team2Match.getStatMatch().getId()).get();
        System.out.println(statMatch2);
        StatMatch statMatch;
        StatMatchFuture result = new StatMatchFuture();
        if (twoTeamMatch != null) {
            statMatch = statMatchRepository.findById(twoTeamMatch.getStatMatch().getId()).get();
            float longestRun = mainAnalysisFull(statMatch.getLongestRun(),statMatch1.getLongestRun(),statMatch2.getLongestRun());
            float timesTied = mainAnalysisFull(statMatch.getTimesTied(),statMatch1.getTimesTied(),statMatch2.getTimesTied());
            float leadChanges = mainAnalysisFull(statMatch.getLeadChanges(),statMatch1.getLeadChanges(),statMatch2.getLeadChanges());
            result.setLongestRun(longestRun);
            result.setLeadChanges(leadChanges);
            result.setTimesTied(timesTied);
        } else {
            float longestRun = mainAnalysis(statMatch1.getLongestRun(),statMatch2.getLongestRun());
            float timesTied = mainAnalysis(statMatch1.getTimesTied(),statMatch2.getTimesTied());
            float leadChanges = mainAnalysis(statMatch1.getLeadChanges(),statMatch2.getLeadChanges());
            result.setLongestRun(longestRun);
            result.setLeadChanges(leadChanges);
            result.setTimesTied(timesTied);
        }
        String winName = team1.getIdStat().getPts() > team2.getIdStat().getPts() ? teamName1 : teamName2;
//        float win = team1.getIdStat().getPts() + team2.getIdStat().getPts(); // 90 110
        result.setWinnersName(winName);
        return result;
    }

    public List<StatPlayerFuture> analyzeStatPlayerMatch(String teamName1, String teamName2) {
        Team team1 = teamRepository.findByName(teamName1);
        Team team2 = teamRepository.findByName(teamName2);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fourHoursAgo = now.minusHours(48);
        Match twoTeamMatch = matchRepository.findByTeam1AndTeam2(team1, team2, fourHoursAgo);
        System.out.println(team1.getName());
        Optional<Match> lateMatch = matchRepository.findLatestMatchByTeam(team1, fourHoursAgo);
        System.out.println(lateMatch.get());
        List<StatPlayerMatch> statMatchTeam2 = statPlayerMatchRepository.findByIdStatMatch(lateMatch.get().getStatMatch());
        List<StatPlayerFuture> statPlayerFutures = new ArrayList<>();
        if (twoTeamMatch != null) {
            List<StatPlayerMatch> statMatchTeam12 = statPlayerMatchRepository.findByIdStatMatch(twoTeamMatch.getStatMatch());
            int count = Math.min(statMatchTeam12.size(), statMatchTeam2.size());
            for (int i = 0; i < count; i++) {
                System.out.println(5);
                StatPlayerMatch statPlayerMatch12 = statMatchTeam12.get(i);
                System.out.println(statPlayerMatch12);
                System.out.println(6);
                System.out.println(statMatchTeam12.size());
                System.out.println(statMatchTeam2.size());
                StatPlayerMatch statPlayerFutureLate = statMatchTeam2.get(i);
                System.out.println(7);
                int idPlayer = statPlayerMatch12.getIdPlayer().getId();
                StatPlayer statMatchTeam1 = statPlayerRepository.findById(idPlayer).get();
                System.out.println(8);
                StatPlayerFuture statTeamFuture = new StatPlayerFuture();
                statTeamFuture.setPts(mainAnalysisFull(statPlayerMatch12.getPts(), statMatchTeam1.getPts(), statPlayerFutureLate.getPts()));
                statTeamFuture.setFg(mainAnalysisFull(statPlayerMatch12.getFg(), statMatchTeam1.getFg(), statPlayerFutureLate.getFg()));
                statTeamFuture.setThreePoints(mainAnalysisFull(statPlayerMatch12.getThreePoints(), statMatchTeam1.getThreePoints(), statPlayerFutureLate.getThreePoints()));
                statTeamFuture.setFt(mainAnalysisFull(statPlayerMatch12.getFt(), statMatchTeam1.getFt(), statPlayerFutureLate.getFt()));
                statTeamFuture.setReb(mainAnalysisFull(statPlayerMatch12.getReb(), statMatchTeam1.getReb(), statPlayerFutureLate.getReb()));
                statTeamFuture.setAst(mainAnalysisFull(statPlayerMatch12.getAst(), statMatchTeam1.getAst(), statPlayerFutureLate.getAst()));
                statTeamFuture.setTov(mainAnalysisFull(statPlayerMatch12.getTov(), statMatchTeam1.getTov(), statPlayerFutureLate.getTov()));
                statTeamFuture.setStl(mainAnalysisFull(statPlayerMatch12.getStl(), statMatchTeam1.getStl(), statPlayerFutureLate.getStl()));
                statTeamFuture.setBlk(mainAnalysisFull(statPlayerMatch12.getBlk(), statMatchTeam1.getBlk(), statPlayerFutureLate.getBlk()));
                statTeamFuture.setIdPlayer(statPlayerMatch12.getIdPlayer());
                statTeamFuture.setMin(String.valueOf(statMatchTeam1.getMin()));
                statPlayerFutures.add(statTeamFuture);
            }
        } else {
            for (int i = 0; i < statMatchTeam2.size(); i++) {
                System.out.println(62);
                StatPlayerMatch statPlayerFutureLate = statMatchTeam2.get(i);
                System.out.println(statPlayerFutureLate);
                int idPlayer = statPlayerFutureLate.getIdPlayer().getId();
                StatPlayer statMatchTeam1 = statPlayerRepository.findById(idPlayer).get();
                System.out.println(statMatchTeam1);
                StatPlayerFuture statTeamFuture = new StatPlayerFuture();
                statTeamFuture.setPts(mainAnalysis(statMatchTeam1.getPts(), statPlayerFutureLate.getPts()));
                statTeamFuture.setFg(mainAnalysis(statMatchTeam1.getFg(), statPlayerFutureLate.getFg()));
                statTeamFuture.setThreePoints(mainAnalysis(statMatchTeam1.getThreePoints(), statPlayerFutureLate.getThreePoints()));
                statTeamFuture.setFt(mainAnalysis(statMatchTeam1.getFt(), statPlayerFutureLate.getFt()));
                statTeamFuture.setReb(mainAnalysis(statMatchTeam1.getReb(), statPlayerFutureLate.getReb()));
                statTeamFuture.setAst(mainAnalysis(statMatchTeam1.getAst(), statPlayerFutureLate.getAst()));
                statTeamFuture.setTov(mainAnalysis(statMatchTeam1.getTov(), statPlayerFutureLate.getTov()));
                statTeamFuture.setStl(mainAnalysis(statMatchTeam1.getStl(), statPlayerFutureLate.getStl()));
                statTeamFuture.setBlk(mainAnalysis(statMatchTeam1.getBlk(), statPlayerFutureLate.getBlk()));
                statTeamFuture.setIdPlayer(statPlayerFutureLate.getIdPlayer());
                statTeamFuture.setMin(String.valueOf(statMatchTeam1.getMin()));
                statPlayerFutures.add(statTeamFuture);
            }

        }
        return statPlayerFutures;
    }

    public StatTeamFuture analyzeStatTeam(String teamName1, String teamName2) {
        Team team1 = teamRepository.findByName(teamName1);
        Team team2 = teamRepository.findByName(teamName2);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fourHoursAgo = now.minusHours(48);
        Match twoTeamMatch = matchRepository.findByTeam1AndTeam2(team1, team2, fourHoursAgo);
        StatTeamSeason statMatchTeam1 = statTeamSeasonRepository.findById(team1.getId()).get();
        Optional<Match> lateMatch = matchRepository.findLatestMatchByTeam(team1, fourHoursAgo);
        StatMatchTeam statMatchTeam2 = statMatchTeamRepository.findByIdStatMatchAndIdTeam(lateMatch.get().getStatMatch(), team1);
        StatTeamFuture statTeamFuture = new StatTeamFuture();
        if (twoTeamMatch != null) {
            System.out.println(twoTeamMatch);
            StatMatchTeam statMatchTeam12 = statMatchTeamRepository.findByIdStatMatchAndIdTeam(twoTeamMatch.getStatMatch(), team1);
            statTeamFuture.setPts(mainAnalysisFull(statMatchTeam12.getPts(), statMatchTeam1.getPts(), statMatchTeam2.getPts()));
            statTeamFuture.setFg(mainAnalysisFull(statMatchTeam12.getFg(), statMatchTeam1.getFg(), statMatchTeam2.getFg()));
            statTeamFuture.setThreePoints(mainAnalysisFull(statMatchTeam12.getThreePoints(), statMatchTeam1.getThreePoints(), statMatchTeam2.getThreePoints()));
            statTeamFuture.setFt(mainAnalysisFull(statMatchTeam12.getFt(), statMatchTeam1.getFt(), statMatchTeam2.getFt()));
            statTeamFuture.setReb(mainAnalysisFull(statMatchTeam12.getReb(), statMatchTeam1.getReb(), statMatchTeam2.getReb()));
            statTeamFuture.setAst(mainAnalysisFull(statMatchTeam12.getAst(), statMatchTeam1.getAst(), statMatchTeam2.getAst()));
            statTeamFuture.setTov(mainAnalysisFull(statMatchTeam12.getTov(), statMatchTeam1.getTov(), statMatchTeam2.getTov()));
            statTeamFuture.setStl(mainAnalysisFull(statMatchTeam12.getStl(), statMatchTeam1.getStl(), statMatchTeam2.getStl()));
            statTeamFuture.setBlk(mainAnalysisFull(statMatchTeam12.getBlk(), statMatchTeam1.getBlk(), statMatchTeam2.getBlk()));
        } else {
            statTeamFuture.setPts(mainAnalysis(statMatchTeam1.getPts(), statMatchTeam2.getPts()));
            statTeamFuture.setFg(mainAnalysis(statMatchTeam1.getFg(), statMatchTeam2.getFg()));
            statTeamFuture.setThreePoints(mainAnalysis(statMatchTeam1.getThreePoints(), statMatchTeam2.getThreePoints()));
            statTeamFuture.setFt(mainAnalysis(statMatchTeam1.getFt(), statMatchTeam2.getFt()));
            statTeamFuture.setReb(mainAnalysis(statMatchTeam1.getReb(), statMatchTeam2.getReb()));
            statTeamFuture.setAst(mainAnalysis(statMatchTeam1.getAst(), statMatchTeam2.getAst()));
            statTeamFuture.setTov(mainAnalysis(statMatchTeam1.getTov(), statMatchTeam2.getTov()));
            statTeamFuture.setStl(mainAnalysis(statMatchTeam1.getStl(), statMatchTeam2.getStl()));
            statTeamFuture.setBlk(mainAnalysis(statMatchTeam1.getBlk(), statMatchTeam2.getBlk()));
        }
        return statTeamFuture;
    }

    public float mainAnalysis(float s1, float s2) {
        return s1 * 0.5f + s2 * 0.5f;
    }
    public float mainAnalysisFull(float s, float s1, float s2) {
        return s * 0.7f + s1 * 0.15f + s2 * 0.15f;
    }
//    public <T> setMainParam(T obj, T p1, T p2, T p3) {
//        return ;
//    }
}
