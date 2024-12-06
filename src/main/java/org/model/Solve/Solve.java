package org.model.Solve;

import org.model.Coordinate;
import org.model.MapMatrix;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Solve {
    MapMatrix map;
    Coordinate beginPlayer;
    HashSet<Coordinate> Walls, Goals, beginBoxes;

    static class Pair<T1,T2> {
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

    public Solve(MapMatrix map) {
        this.map = map;
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

    boolean isLegalAction(Action action, Coordinate player, HashSet<Coordinate> Boxes) {
        Coordinate next;
        if(action.push()) {
            next = new Coordinate(player.x + 2 * action.move_1, player.y + 2 * action.move_2);
        } else {
            next = new Coordinate(player.x + action.move_1, player.y + action.move_2);
        }
        return !setAdd(Walls, Boxes).contains(next);
    }

    ArrayList<Action> legalActions(Coordinate player, HashSet<Coordinate> Boxes) {
        ArrayList<Action> actions = new ArrayList<>();
        Action[] AllActions = new Action[] {
                new Action(0, -1), new Action(0, 1), new Action(-1, 0), new Action(1, 0)
        };
        for(Action action : AllActions){
            if(Boxes.contains(new Coordinate(player.x + action.move_1, player.y + action.move_2))){
                action.set_push(true);
            }
            if(isLegalAction(action, player, Boxes)){
                actions.add(action);
            }
        }
        return actions;
    }

    Pair<Coordinate, HashSet<Coordinate>> updateState(Coordinate player, HashSet<Coordinate> Boxes, Action action) {
        Coordinate nextPlayer = new Coordinate(player.x + action.move_1, player.y + action.move_2);
        HashSet<Coordinate> nextBoxes = new HashSet<>(Boxes);
        if(action.push()){
            nextBoxes.remove(new Coordinate(player.x + action.move_1, player.y + action.move_2));
            nextBoxes.add(new Coordinate(player.x + 2 * action.move_1, player.y + 2 * action.move_2));
        }
        return new Pair<>(nextPlayer, nextBoxes);
    }


    boolean isFailed() {
        //TODO 判断一些典型类型的已失败
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

    int cost(ArrayList<Action> actions) { // 没推箱子走的步数 激励推箱子
        int len = 0;
        for(Action action : actions) {
            if(!action.push()) {
                len++;
            }
        }
        return len;
    }

    void aStarSearch() {
        State startState = new State(beginPlayer, beginBoxes, heuristic(beginBoxes));
        PriorityQueue<State> frontier = new PriorityQueue<>();
        frontier.add(startState);
        HashSet<HashSet<Coordinate>> explored = new HashSet<>();
        PriorityQueue<Pair<Action, Integer>> actions = new PriorityQueue<>();
        actions.add(new Pair<>(new Action(0, 0), heuristic(beginBoxes)));
        while(!frontier.isEmpty()) {
            State node = frontier.poll();
            Action nodeAction = actions.poll().getFirst();
            if(isEndState(node.boxes)) {
                for(Pair<Action, Integer> action : actions) {
                    System.out.println(action.getFirst().move);
                }
                return;
            }
            if(!explored.contains(node.boxes)) { // 这里似乎因为equalsto的问题会有bug
                explored.add(node.boxes);

            }
        }
    }

}
