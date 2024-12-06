package org.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.HashSet;

public class Solve {
    MapMatrix map;
    Coordinate player;
    HashSet<Coordinate> Walls, Boxes, Goals;

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

    boolean isFailed() {
        //TODO 判断一些典型类型的已失败
        return false;
    }

    int manhattan(Coordinate a, Coordinate b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    int heuristic() {
        int distance = 0;
        HashSet<Coordinate> completes = setIntersect(Boxes, Goals);
        Object[] leftBoxes = setSub(Boxes, completes).toArray();
        Object[] leftGoals = setSub(Goals, completes).toArray();

        for(int i = 0; i < leftBoxes.length; i++) {
            distance += manhattan((Coordinate)leftBoxes[i], (Coordinate)leftGoals[i]);
        }
        return distance;
    }

}
