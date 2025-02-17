package main.iscourseworkback.schedule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDate {
    @JsonProperty("gameDate")
    private String gameDate;

    @JsonProperty("games")
    private List<Game> games;


}