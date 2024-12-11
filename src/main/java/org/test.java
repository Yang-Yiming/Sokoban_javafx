package org;

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
                System.out.println(solve.aStarSearch());
            } else
                solve.aStarSearch();
            total += System.currentTimeMillis() - startTime;
        }
        System.out.printf("Test: %s - %d times, average time: %f ms%n", Name, times, total / times);
    }


    public static void main(String[] args) {
//        Test(mapdata.maps[1], "EasyMap", 100);
//        Test(mapdata.maps[4], "HardMap", 20);
//        Test(new int[][] {
//                        {1, 1, 1, 1, 1, 1, 0, 0},
//                        {1, 0, 0, 0, 0, 1, 1, 1},
//                        {1, 0, 0, 0, 8, 8, 0, 1},
//                        {1, 2, 0, 2, 2, 4, 0, 1},
//                        {1, 0, 0, 1, 0, 8, 0, 1},
//                        {1, 1, 1, 1, 1, 1, 1, 1}},
//                "NoSolveMap", 20);
//        Solve solve = new Solve(new MapMatrix(mapdata.huge_maps[0]));
        HashMap<Coordinate, Integer> map = new HashMap<>();
        // 0 1 0 0
        // 0 1 0 0
        // 0 1 0 1
        // 0 0 0 1
        map.put(new Coordinate(1, 0), -1);
        map.put(new Coordinate(1, 1), -1);
        map.put(new Coordinate(1, 2), -1);
        map.put(new Coordinate(3, 2), -1);
        map.put(new Coordinate(3, 3), -1);

        FindPath findPath = new FindPath(map, new Coordinate(0, 0), new Coordinate(2, 1));
        System.out.println(findPath.findPath());
    }
}
