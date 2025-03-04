package main.iscourseworkback.present.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StatTeamSeason {
    @Id
    private int id;
    private float gp;
    private float win;
    private float pts;
    private float fg;
    private float threePoints;
    private float ft;
    private float reb;
    private float ast;
    private float tov;
    private float stl;
    private float blk;

    @Override
    public String toString() {
        return "StatTeamSeason{" +
                "id=" + id +
                ", gp=" + gp +
                ", win=" + win +
                ", pts=" + pts +
                ", threePoints=" + threePoints +
                ", ft=" + ft +
                ", reb=" + reb +
                ", ast=" + ast +
                ", tov=" + tov +
                ", stl=" + stl +
                ", blk=" + blk +
                '}';
    }
}
