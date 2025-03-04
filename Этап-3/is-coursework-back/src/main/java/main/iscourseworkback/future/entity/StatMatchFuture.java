package main.iscourseworkback.future.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StatMatchFuture {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String winnersName;
    private float timesTied;
    private float longestRun;
    private float leadChanges;

    @Override
    public String toString() {
        return "StatMatchFuture{" +
                "id=" + id +
                ", winnersName='" + winnersName + '\'' +
                ", timesTied=" + timesTied +
                ", longestRun=" + longestRun +
                ", leadChanges=" + leadChanges +
                '}';
    }
}
