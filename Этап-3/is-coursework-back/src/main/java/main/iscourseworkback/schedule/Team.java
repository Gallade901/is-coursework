package main.iscourseworkback.schedule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
    @JsonProperty("teamId")
    private int id;
    @JsonProperty("teamName")
    private String teamName;

    @JsonProperty("teamCity")
    private String teamCity;


}
