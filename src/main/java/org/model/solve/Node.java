package org.model.solve;

public class Node implements Comparable<Node> {
    int x;
    int y;
    Node parent;
    double gScore; // 起点到当前的实际代价
    double hScore; // 当前到目标的预估代价
    double fScore; // F = G + H

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.parent = null;
        this.gScore = Double.MAX_VALUE;
        this.hScore = 0;
        this.fScore = Double.MAX_VALUE;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.fScore, other.fScore);
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}
