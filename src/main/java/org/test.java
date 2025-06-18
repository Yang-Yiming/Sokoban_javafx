package org;

import org.data.mapdata;
import org.model.Coordinate;
import org.model.MapMatrix;
import org.model.Solve.Solve;
import org.model.Solve.SimulatedAnnealing;
import org.model.Solve.GeneticAlgorithm;
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

    // 比较三种算法（A*、模拟退火、遗传算法）的性能
    static void CompareAlgorithms(int[][] map, String mapName) {
        MapMatrix mapMatrix = new MapMatrix(map);
        System.out.println("====== 地图: " + mapName + " 算法性能比较 ======");

        // 测试 A* 算法
        long startTime = System.currentTimeMillis();
        Solve aStarSolver = new Solve(mapMatrix);
        String aStarResult = aStarSolver.aStarSearch();
        long aStarTime = System.currentTimeMillis() - startTime;
        System.out.printf("A* 算法: %s, 耗时: %d ms%n",
            aStarResult.equals("N") ? "无解" : "有解", aStarTime);

        // 测试模拟退火算法
        startTime = System.currentTimeMillis();
        SimulatedAnnealing saSolver = new SimulatedAnnealing(mapMatrix);
        String saResult = saSolver.solve();
        long saTime = System.currentTimeMillis() - startTime;
        System.out.printf("模拟退火算法: %s, 耗时: %d ms%n",
            saResult.equals("N") ? "无解" : "有解", saTime);

        // 测试遗传算法
        startTime = System.currentTimeMillis();
        GeneticAlgorithm gaSolver = new GeneticAlgorithm(mapMatrix);
        String gaResult = gaSolver.solve();
        long gaTime = System.currentTimeMillis() - startTime;
        System.out.printf("遗传算法: %s, 耗时: %d ms%n",
            gaResult.equals("N") ? "无解" : "有解", gaTime);

        System.out.println("======= 性能比较结束 =======\n");
    }


    public static void main(String[] args) {
        System.out.println("开始算法性能对比测试");

        // 使用简单、中等和困难的地图测试三种算法
        if (mapdata.maps != null && mapdata.maps.length > 0) {
            CompareAlgorithms(mapdata.maps[0], "简单地图 #0");
        }

        if (mapdata.hard_maps != null && mapdata.hard_maps.length > 0) {
            CompareAlgorithms(mapdata.hard_maps[0], "困难地图 #0");
        }

        if (mapdata.huge_maps != null && mapdata.huge_maps.length > 0) {
            CompareAlgorithms(mapdata.huge_maps[0], "巨型地图 #0");
        }

        // 如果需要旧的测试方法，请取消下面的注释
        // 仅测试A*算法
        /*
        for(int i = 0; i < mapdata.hard_maps.length; i++) {
            Test(mapdata.hard_maps[i], "map " + i, 1);
        }
        */
    }
}
