package main.iscourseworkback.present.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player {
    @Id
    private int id;
    private String name;
//    private String position;
//    private int number;
//    private float height;
//    private float weight;
    @ManyToOne(cascade = CascadeType.ALL)
    private Team idTeam;
}
