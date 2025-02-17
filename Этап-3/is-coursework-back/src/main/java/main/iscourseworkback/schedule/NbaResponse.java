package main.iscourseworkback.schedule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NbaResponse {
    @JsonProperty("leagueSchedule")
    private LeagueSchedule leagueSchedule;
}
