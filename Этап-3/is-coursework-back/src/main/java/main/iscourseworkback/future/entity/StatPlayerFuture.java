package main.iscourseworkback.future.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.iscourseworkback.present.entity.Player;
import main.iscourseworkback.present.entity.StatMatch;

@Entity
@Getter
@Setter
public class StatPlayerFuture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String min;
    private float pts;
    private float fg;
    private float threePoints;
    private float ft;
    private float reb;
    private float ast;
    private float tov;
    private float stl;
    private float blk;
    @ManyToOne
    private Player idPlayer;
    @ManyToOne
    private StatMatchFuture idStatMatch;
}
