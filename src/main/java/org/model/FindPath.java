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

    public static final int OBSTACLE = -1;

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

    public String findPath() {
        openList.add(new node(start, 0, manhatten(start, target), null));
        while (!openList.isEmpty()) {
            node current = openList.poll();
            if (current.coordinate.equals(target)) {
                return constructPath(current);
            }
            closedList.add(new Coordinate(current.coordinate.x, current.coordinate.y));
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i + j != 1) continue;
                    int x = current.coordinate.x + i;
                    int y = current.coordinate.y + j;
                    //if (x < 0 || y < 0 || y >= map[0].length) continue;
                    if (map.getOrDefault(new Coordinate(x,y),0) == OBSTACLE || closedList.contains(new Coordinate(x,y))) continue;
                    int g = current.g + 1;
                    int h = manhatten(new Coordinate(x, y), target);
                    node next = new node(new Coordinate(x, y), g, h, current);

                    boolean flag = false;
                    for(node n: openList){
                        if(n.equals(next)){
                            if(n.g > next.g){
                                n.parent = current;
                                n.g = next.g;
                                n.f = n.g + n.h;
                                flag = true;
                                break;
                            }
                        }
                    } if(flag) break;

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
