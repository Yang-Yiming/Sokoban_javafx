package org;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.data.mapdata;
import org.model.MapMatrix;
import org.model.Solve.Solve;
import org.model.config;
import org.view.level.InfiniteLevelManager;

import java.io.IOException;
import java.util.Map;

public class test {
    static void Test(int[][] map, String Name){
        MapMatrix mapMatrix = new MapMatrix(map);
        Solve solve = new Solve(mapMatrix);
        long startTime = System.currentTimeMillis();
        solve.aStarSearch();
        System.out.println("Test: "+ Name +" -- Time: " + (System.currentTimeMillis() - startTime) + "ms");
    }


    public static void main(String[] args) {
        Test(mapdata.maps[0], "Map1");
        Test(mapdata.maps[1], "Map2");
        Test(mapdata.maps[2], "Map3");
        Test(mapdata.maps[3], "Map4");
        Test(mapdata.maps[4], "Map5");
        Test(new int[][] {
                        {1, 1, 1, 1, 1, 1, 0, 0},
                        {1, 0, 0, 0, 0, 1, 1, 1},
                        {1, 0, 0, 0, 8, 8, 0, 1},
                        {1, 2, 0, 2, 2, 4, 0, 1},
                        {1, 0, 0, 1, 0, 8, 0, 1},
                        {1, 1, 1, 1, 1, 1, 1, 1}}, 
                "NoSolveTest");

    }
}
