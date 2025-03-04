package main.iscourseworkback.future.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.iscourseworkback.present.entity.StatMatch;
import main.iscourseworkback.present.entity.Team;

import java.util.Date;


@Entity
@Getter
@Setter
public class MatchFuture {
    @Id
    private Integer id;
    private String location;
    private Date date;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private StatMatchFuture statMatch;
    @ManyToOne(cascade = CascadeType.ALL)
    private Team team1;
    @ManyToOne(cascade = CascadeType.ALL)
    private Team team2;
}
