package main.iscourseworkback.future.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import main.iscourseworkback.future.entity.StatPlayerFuture;
import main.iscourseworkback.present.entity.StatPlayerMatch;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StatPlayersMatchFutureDTO {
    private List<StatPlayerFuture> statPlayersTeam1;
    private List<StatPlayerFuture> statPlayersTeam2;
    private String teamName1;
    private String teamName2;
    private int team1Id;
    private int team2Id;
}
