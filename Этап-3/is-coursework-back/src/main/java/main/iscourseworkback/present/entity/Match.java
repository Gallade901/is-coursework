package main.iscourseworkback.present.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Match {
    @Id
    private Integer id;
    private String location;
    private Date date;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private StatMatch statMatch;
    @ManyToOne(cascade = CascadeType.ALL)
    private Team team1;
    @ManyToOne(cascade = CascadeType.ALL)
    private Team team2;

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", statMatch=" + statMatch +
                ", team1=" + team1 +
                ", team2=" + team2 +
                '}';
    }
}
