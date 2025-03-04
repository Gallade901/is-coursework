package main.iscourseworkback.present.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class StatMatch {
    @Id
    private int id;
    private String winnersName;
    private float timesTied;
    private float longestRun;
    private float leadChanges;

    @Override
    public String toString() {
        return "StatMatch{" +
                "id=" + id +
                ", winnersName='" + winnersName + '\'' +
                ", timesTied=" + timesTied +
                ", longestRun=" + longestRun +
                ", leadChanges=" + leadChanges +
                '}';
    }
}
