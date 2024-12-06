package org.model.Solve;

import org.model.Coordinate;

import java.util.ArrayList;
import java.util.HashSet;

public class State {
    Coordinate player;
    HashSet<Coordinate> boxes;
    int heuristic;

    public State(Coordinate player, HashSet<Coordinate> boxes, int heuristic) {
        this.player = new Coordinate(player.x, player.y);
        this.boxes = new HashSet<>(boxes);
        this.heuristic = heuristic;
    }
}
