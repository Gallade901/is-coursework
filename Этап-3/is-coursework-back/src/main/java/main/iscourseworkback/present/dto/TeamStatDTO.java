package main.iscourseworkback.present.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import main.iscourseworkback.present.entity.StatTeamSeason;

@Getter
@Setter
@AllArgsConstructor
public class TeamStatDTO {
    private StatTeamSeason statTeamSeason;
    private String teamName;
}
