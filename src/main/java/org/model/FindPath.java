package org.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

// 用于levelselect

class node implements Comparable<node> {
    Coordinate coordinate;
    int g; int f; int h;
    node parent;

    public node(Coordinate coordinate, int g, int h, node parent) {
        this.coordinate = coordinate;
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.parent = parent;
    }

    @Override
    public int compareTo(node o) {
        return this.f - o.f;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof node) {
            node n = (node) obj;
            return this.coordinate.equals(n.coordinate);
        }
        return false;
    }

}


public class FindPath {
    public int manhatten (Coordinate a, Coordinate b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    public static final int OBSTACLE = -1; // 负数全都是障碍物

    HashMap<Coordinate, Integer> map;
    Coordinate start;
    Coordinate target;
    PriorityQueue<node> openList;
    ArrayList<Coordinate> closedList;

    public FindPath(HashMap<Coordinate, Integer> map, Coordinate start, Coordinate target) {
        this.map = map;
        this.start = start;
        this.target = target;
        this.openList = new PriorityQueue<>();
        this.closedList = new ArrayList<>();
    }

    int[] dx = new int[]{1, 0, -1, 0};
    int[] dy = new int[]{0, 1, 0, -1};

    public String findPath() {
        openList.add(new node(start, 0, manhatten(start, target), null));
        while (!openList.isEmpty()) {
            node current = openList.poll();
            if (current.coordinate.equals(target)) {
                return constructPath(current);
            }
            closedList.add(current.coordinate);
            for (int i = 0; i < 4; i++) {
                int x = current.coordinate.x + dx[i];
                int y = current.coordinate.y + dy[i];
                Coordinate next = new Coordinate(x, y);
                if (map.getOrDefault(next, 0) > OBSTACLE && !closedList.contains(next)) {
                    node nextNode = new node(next, current.g + 1, manhatten(next, target), current);
                    if (!openList.contains(nextNode)) {
                        openList.add(nextNode);
                    } else {
                        for (node n : openList) {
                            if (n.equals(nextNode) && n.g > nextNode.g) {
                                n.g = nextNode.g;
                                n.f = n.g + n.h;
                                n.parent = current;
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    String constructPath(node node) {
        String move = "";
        while (node.parent != null) {
            if (node.coordinate.x - node.parent.coordinate.x == 1) {
                move = "d" + move;
            } else if (node.coordinate.x - node.parent.coordinate.x == -1) {
                move = "a" + move;
            } else if (node.coordinate.y - node.parent.coordinate.y == 1) {
                move = "s" + move;
            } else {
                move = "w" + move;
            }
            node = node.parent;
        }
        return move;
    }

    public static String findPath(HashMap<Coordinate,Integer>map, Coordinate start, Coordinate target) {
        FindPath findPath = new FindPath(map, start, target);
        return findPath.findPath();
    }
}
