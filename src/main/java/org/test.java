package org;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.data.mapdata;
import org.model.MapMatrix;
import org.model.config;
import org.model.solve.Solve;
import org.view.level.InfiniteLevelManager;

import java.io.IOException;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        MapMatrix map = new MapMatrix(mapdata.maps[0]);
        Solve solve = new Solve(map);
        System.out.println(solve.solve());
    }
}
