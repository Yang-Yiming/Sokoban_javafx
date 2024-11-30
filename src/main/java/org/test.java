package org;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.model.config;
import org.view.level.InfiniteLevelManager;

import java.io.IOException;
import java.util.Map;

public class test extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Scene scene = new Scene(new Pane(), config.ScreenWidth, config.ScreenHeight);
        primaryStage.setScene(scene);
        InfiniteLevelManager manager = new InfiniteLevelManager(primaryStage);
        manager.start();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
