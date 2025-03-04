package main.iscourseworkback.schedule;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {
    @JsonProperty("gameId")
    private String gameId;

    @JsonProperty("gameDateUTC")
    private String gameDateUTC;

    @JsonProperty("homeTeam")
    private Team homeTeam;

    @JsonProperty("awayTeam")
    private Team awayTeam;

    @JsonProperty("arenaName")
    private String arenaName;

}