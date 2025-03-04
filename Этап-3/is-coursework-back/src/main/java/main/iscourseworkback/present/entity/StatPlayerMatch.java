package main.iscourseworkback.present.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StatPlayerMatch  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String min;
    private int pts;
    private float fg;
    private float threePoints;
    private float ft;
    private int reb;
    private int ast;
    private int tov;
    private int stl;
    private int blk;
    @ManyToOne
    private Player idPlayer;
    @ManyToOne
    private StatMatch idStatMatch;

    @Override
    public String toString() {
        return "StatPlayerMatch{" +
                "id=" + id +
                ", min='" + min + '\'' +
                ", pts=" + pts +
                ", fg=" + fg +
                ", threePoints=" + threePoints +
                ", ft=" + ft +
                ", reb=" + reb +
                ", ast=" + ast +
                ", tov=" + tov +
                ", stl=" + stl +
                ", blk=" + blk +
                ", idPlayer=" + idPlayer +
                ", idStatMatch=" + idStatMatch +
                '}';
    }
}
