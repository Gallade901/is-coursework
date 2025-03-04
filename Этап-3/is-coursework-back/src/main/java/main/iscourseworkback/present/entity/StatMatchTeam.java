package main.iscourseworkback.present.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StatMatchTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
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
    private Team idTeam;
    @ManyToOne
    private StatMatch idStatMatch;

    @Override
    public String toString() {
        return "StatMatchTeam{" +
                "id=" + id +
                ", pts=" + pts +
                ", fg=" + fg +
                ", threePoints=" + threePoints +
                ", ft=" + ft +
                ", reb=" + reb +
                ", ast=" + ast +
                ", tov=" + tov +
                ", stl=" + stl +
                ", blk=" + blk +
                ", idTeam=" + idTeam +
                ", idStatMatch=" + idStatMatch +
                '}';
    }
}
