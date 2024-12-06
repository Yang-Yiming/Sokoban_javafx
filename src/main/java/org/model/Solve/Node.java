package org.model.Solve;

import org.model.Coordinate;

import java.util.ArrayList;
import java.util.HashSet;

class Stage {
    Coordinate player;
    HashSet<Coordinate> boxes;

    public Stage(Coordinate player, HashSet<Coordinate> boxes) {
        this.player = new Coordinate(player.x, player.y);
        this.boxes = new HashSet<>(boxes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass()!= o.getClass()) return false;
        Stage that = (Stage) o;
        return this.player.equals(that.player) && this.boxes.equals(that.boxes);
    }

    @Override
    public int hashCode() {
        return this.player.hashCode() + this.boxes.hashCode();
    }
}

public class Node {
    public ArrayList<Stage> stages;
    public Node(ArrayList<Stage> stages) {
        this.stages = new ArrayList<>(stages);
    }
    public Node(Node node){
        this.stages = new ArrayList<>(node.stages);
    }
    public Node() {
        this.stages = new ArrayList<>();
    }
    public Node(Stage stage) {
        this.stages = new ArrayList<>();
        this.stages.add(stage);
    }

    public Stage getLast(){
        return stages.getLast();
    }
    public void add(Stage stage){
        stages.add(stage);
    }
}
