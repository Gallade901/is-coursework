package main.iscourseworkback.schedule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeagueSchedule {
    @JsonProperty("gameDates")
    private List<GameDate> gameDates;

}