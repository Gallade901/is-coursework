package main.iscourseworkback.present.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StatPlayer {
    @Id
    private int id;
    private float min;
    private float gp;
    private float pts;
    private float fg;
    private float threePoints;
    private float ft;
    private float reb;
    private float ast;
    private float tov;
    private float stl;
    private float blk;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Player player;

    @Override
    public String toString() {
        return "StatPlayer{" +
                "id=" + id +
                ", min=" + min +
                ", gp=" + gp +
                ", pts=" + pts +
                ", fg=" + fg +
                ", threePoints=" + threePoints +
                ", ft=" + ft +
                ", reb=" + reb +
                ", ast=" + ast +
                ", tov=" + tov +
                ", stl=" + stl +
                ", blk=" + blk +
                ", player=" + player +
                '}';
    }
}
