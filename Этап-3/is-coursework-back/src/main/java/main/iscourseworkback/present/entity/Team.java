package main.iscourseworkback.present.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Team {
    @Id
    private int id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private StatTeamSeason idStat;
}
