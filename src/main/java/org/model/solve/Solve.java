package org.model.solve;

import org.model.MapMatrix;

import java.util.*;

public class Solve {
    private MapMatrix map;
    private List<Node> boxes;
    private Node player;
    private List<Node> goals;

    public Solve(MapMatrix map) {
        this.map = map;
        this.boxes = new ArrayList<>();
        this.goals = new ArrayList<>();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.hasBox(x, y)) {
                    boxes.add(new Node(x, y));
                }
                if (map.hasPlayer(x, y)) {
                    player = new Node(x, y);
                }
                if (map.hasGoal(x, y)) {
                    goals.add(new Node(x, y));
                }
            }
        }
    }

    public List<Node> solve() {
        AStar aStar = new AStar(map);
        return aStar.findPath(boxes, player, goals);
    }
}
