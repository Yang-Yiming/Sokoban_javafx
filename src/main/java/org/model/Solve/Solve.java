package org.model.Solve;

import org.model.Coordinate;
import org.model.GameMap;
import org.model.MapMatrix;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.PriorityQueue;

public class Solve {
    GameMap map;
    Coordinate beginPlayer;
    HashSet<Coordinate> Walls, Goals, beginBoxes;

    static class Pair<T1,T2 extends Comparable<T2>> implements Comparable<Pair<T1,T2>> {
        T1 first;
        T2 second;
        Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }
        public T1 getFirst() {
            return first;
        }
        public T2 getSecond() {
            return second;
        }

        @Override
        public int compareTo(Pair<T1, T2> o) {
            return this.second.compareTo(o.second);
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass()!= o.getClass()) return false;
            Pair<?,?> that = (Pair<?,?>) o;
            return this.first.equals(that.first) && this.second.equals(that.second);
        }
        @Override
        public int hashCode() {
            return Objects.hash(first,second);
        }
    }

    <T> HashSet<T> setAdd(HashSet<T> a, HashSet<T> b) { //取并集
        HashSet<T> res = new HashSet<>();
        res.addAll(a);
        res.addAll(b);
        return res;
    }

    <T> HashSet<T> setSub(HashSet<T> a, HashSet<T> b) { //取差集
        HashSet<T> res = new HashSet<>();
        res.addAll(a);
        res.removeAll(b);
        return res;
    }

    <T> HashSet<T> setIntersect(HashSet<T> a, HashSet<T> b) { //取交集
        HashSet<T> res = new HashSet<>();
        res.addAll(a);
        res.retainAll(b);
        return res;
    }

    static Coordinate move(char action) {
        if(action == 'W' || action == 'w') {
            return new Coordinate(0, -1);
        } else if(action == 'A' || action == 'a'){
            return new Coordinate(-1, 0);
        } else if(action == 'S' || action == 's'){
            return new Coordinate(0, 1);
        } else if(action == 'D' || action == 'd'){
            return new Coordinate(1, 0);
        } else {
            return new Coordinate(0, 0);
        }
    }

    public Solve(GameMap map) {
        this.map = map;
        this.Walls = new HashSet<>();
        this.Goals = new HashSet<>();
        this.beginBoxes = new HashSet<>();
        for(int i = 0; i < map.getHeight(); i++) {
            for(int j = 0; j < map.getWidth(); j++) {
                if(map.hasBox(j, i)) {
                    beginBoxes.add(new Coordinate(j, i));
                }
                if(map.hasPlayer(j, i)) {
                    beginPlayer = new Coordinate(j, i);
                }
                if(map.hasWall(j, i)) {
                    Walls.add(new Coordinate(j, i));
                }
                if(map.hasGoal(j, i)) {
                    Goals.add(new Coordinate(j, i));
                }
            }
        }
    }

    boolean isEndState(HashSet<Coordinate> Boxes) {
        return setIntersect(Boxes, Goals).size() == Goals.size();
    }

    boolean isLegalAction(char action, Coordinate player, HashSet<Coordinate> Boxes) {
        Coordinate next;
        if(Character.isUpperCase(action)) {
            next = new Coordinate(player.x + 2 * move(action).x, player.y + 2 * move(action).y);
        } else {
            next = new Coordinate(player.x + move(action).x, player.y + move(action).y);
        }
        return !setAdd(Walls, Boxes).contains(next);
    }

    ArrayList<Character> legalActions(Coordinate player, HashSet<Coordinate> Boxes) {
        ArrayList<Character> actions = new ArrayList<>();
        char[] AllActions = new char[] {'w', 'a', 's', 'd'};

        for(char action : AllActions){
            if(Boxes.contains(new Coordinate(player.x + move(action).x, player.y + move(action).y))){
                action = Character.toUpperCase(action);
            }
            if(isLegalAction(action, player, Boxes)){
                actions.add(action);
            }
        }
        return actions;
    }

    Pair<HashSet<Coordinate>, Coordinate> updateState(Coordinate player, HashSet<Coordinate> Boxes, char action) {
        Coordinate nextPlayer = new Coordinate(player.x + move(action).x, player.y + move(action).y);
        HashSet<Coordinate> nextBoxes = new HashSet<>(Boxes);
        if(Character.isUpperCase(action)) {
            nextBoxes.remove(new Coordinate(player.x + move(action).x, player.y + move(action).y));
            nextBoxes.add(new Coordinate(player.x + 2 * move(action).x, player.y + 2 * move(action).y));
        }
        return new Pair<>(nextBoxes, nextPlayer);
    }


    boolean isFailed(HashSet<Coordinate> Boxes) {
        int[][] Patterns = new int[][] {
                {0,1,2,3,4,5,6,7,8},
                {2,5,8,1,4,7,0,3,6},
                {8,7,6,5,4,3,2,1,0},
                {6,3,0,7,4,1,8,5,2},
                {2,1,0,5,4,3,8,7,6},
                {0,3,6,1,4,7,2,5,8},
                {6,7,8,3,4,5,0,1,2},
                {8,5,2,7,4,1,6,3,0}
        };

        for(Coordinate box : Boxes) {
            if(!Goals.contains(box)){
                int[][] board = new int[][] {
                        {box.x-1,box.y-1},{box.x-1,box.y},{box.x-1,box.y+1},
                        {box.x,box.y-1},{box.x,box.y},{box.x,box.y+1},
                        {box.x+1,box.y-1},{box.x+1,box.y},{box.x+1,box.y+1}
                };
                for(int[] pattern : Patterns) {
                    int[][] newboard = new int[9][2];
                    for(int i = 0; i < 9; i++){
                        newboard[i][0] = board[pattern[i]][0];
                        newboard[i][1] = board[pattern[i]][1];
                    }
                    if(Walls.contains(new Coordinate(newboard[0])) && Walls.contains(new Coordinate(newboard[5]))) return true;
                    if(Boxes.contains(new Coordinate(newboard[1])) && Walls.contains(new Coordinate(newboard[2])) && Walls.contains(new Coordinate(newboard[5]))) return true;
                    if(Boxes.contains(new Coordinate(newboard[1])) && Walls.contains(new Coordinate(newboard[2])) && Boxes.contains(new Coordinate(newboard[5]))) return true;
                    if(Boxes.contains(new Coordinate(newboard[1])) && Boxes.contains(new Coordinate(newboard[2])) && Boxes.contains(new Coordinate(newboard[5]))) return true;
                    if(Boxes.contains(new Coordinate(newboard[1])) && Boxes.contains(new Coordinate(newboard[6])) && Walls.contains(new Coordinate(newboard[2])) && Walls.contains(new Coordinate(newboard[3])) && Walls.contains(new Coordinate(newboard[8]))) return true;
                }
            }
        }

        return false;
    }

    int manhattan(Coordinate a, Coordinate b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    int heuristic(HashSet<Coordinate> Boxes) {
        int distance = 0;
        HashSet<Coordinate> completes = setIntersect(Boxes, Goals);
        Object[] leftBoxes = setSub(Boxes, completes).toArray();
        Object[] leftGoals = setSub(Goals, completes).toArray();

        for(int i = 0; i < leftBoxes.length; i++) {
            distance += manhattan((Coordinate)leftBoxes[i], (Coordinate)leftGoals[i]);
        }
        return distance;
    }

    int cost(String actions) { // 没推箱子走的步数 激励推箱子
        int len = 0;
        for(char action : actions.toCharArray()) {
            if(Character.isLowerCase(action)) {
                len++;
            }
        }
        return len;
    }

    public String aStarSearch() {
        Node startNode = new Node(new Stage(beginPlayer,beginBoxes));
        PriorityQueue<Pair<Node, Integer>> frontier = new PriorityQueue<>();
        frontier.add(new Pair<>(startNode, heuristic(beginBoxes)));

        HashSet<Stage> explored = new HashSet<>();
        PriorityQueue<Pair<String, Integer>> actions = new PriorityQueue<>();
        actions.add(new Pair<>("", heuristic(beginBoxes)));

        while(!frontier.isEmpty()) {
            Node node = frontier.poll().getFirst();
            String nodeAction = actions.poll().getFirst();
            if(isEndState(node.getLast().boxes)) {
                return nodeAction;
            }
            if(!explored.contains(node.getLast())) {
                explored.add(node.getLast());
                int Cost = cost(nodeAction);
                for(char action : legalActions(node.getLast().player, node.getLast().boxes)) {
                    Pair<HashSet<Coordinate>, Coordinate> next = updateState(node.getLast().player, node.getLast().boxes, action);
                    Coordinate newPlayer = next.getSecond(); HashSet<Coordinate> newBoxes = next.getFirst();
                    if(isFailed(newBoxes)) {
                        continue;
                    }
                    int Heuristic = heuristic(newBoxes);

                    Node newNode = new Node(node);
                    newNode.add(new Stage(newPlayer, newBoxes));
                    frontier.add(new Pair<>(newNode, Cost + Heuristic));
                    actions.add(new Pair<>(nodeAction + action, Cost + Heuristic));
                }

            }
        }
        return "N";
    }

}
