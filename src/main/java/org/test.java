package org;

import org.data.mapdata;
import org.model.Coordinate;
import org.model.MapMatrix;
import org.model.Solve.Solve;
import org.model.FindPath;

import java.util.HashMap;

public class test {
    static void Test(int[][] map, String Name, int times){
        MapMatrix mapMatrix = new MapMatrix(map);
        Solve solve = new Solve(mapMatrix);
        double total = 0.0;
        for(int i = 0; i < times; i++) {
            long startTime = System.currentTimeMillis();
            if(times == 1){
                System.out.println(solve.aStarSearch().equals("N")?"No solution":"Solution found");
            } else
                solve.aStarSearch();
            total += System.currentTimeMillis() - startTime;
        }
        System.out.printf("Test: %s - %d times, average time: %f ms%n", Name, times, total / times);
    }


    public static void main(String[] args) {
//        for(int i = 0; i < mapdata.maps2.length; i++) {
//            Test(mapdata.maps2[i], "map " + i, 10);
//        }
    }
}
