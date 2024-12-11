package org.model;

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

}


public class FindPath {
    public int manhatten (Coordinate a, Coordinate b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    public static final int OBSTACLE = -1;

    int[][] map;
    Coordinate start;
    Coordinate target;
    PriorityQueue<node> openList;
    boolean[][] closedList;

    public FindPath(int[][] map, Coordinate start, Coordinate target) {
        this.map = map;
        this.start = start;
        this.target = target;
        this.openList = new PriorityQueue<>();
        this.closedList = new boolean[map.length][map[0].length];
    }

    public String findPath() {
        openList.add(new node(start, 0, manhatten(start, target), null));
        while (!openList.isEmpty()) {
            node current = openList.poll();
            if (current.coordinate.equals(target)) {
                return constructPath(current);
            }
            closedList[current.coordinate.x][current.coordinate.y] = true;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i + j != 1) continue;
                    int x = current.coordinate.x + i;
                    int y = current.coordinate.y + j;
                    if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) continue;
                    if (map[x][y] == OBSTACLE || closedList[x][y]) continue;
                    int g = current.g + 1;
                    int h = manhatten(new Coordinate(x, y), target);
                    node next = new node(new Coordinate(x, y), g, h, current);
                    openList.add(next);
                }
            }
        }
        return null;
    }

    String constructPath(node node) {
        String move = "";
        while (node.parent != null) {
            if (node.coordinate.x - node.parent.coordinate.x == 1) {
                move = "D" + move;
            } else if (node.coordinate.x - node.parent.coordinate.x == -1) {
                move = "A" + move;
            } else if (node.coordinate.y - node.parent.coordinate.y == 1) {
                move = "S" + move;
            } else {
                move = "W" + move;
            }
            node = node.parent;
        }
        return move;
    }

    public static String findPath(int[][] map, Coordinate start, Coordinate target) {
        FindPath findPath = new FindPath(map, start, target);
        return findPath.findPath();
    }
}
