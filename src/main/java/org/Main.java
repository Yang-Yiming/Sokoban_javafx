package org;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.view.level.ClassicLevelManager;

import java.util.Objects;

import static javafx.application.Application.launch;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage){
        Pane root = new Pane();
        ClassicLevelManager levelManager = new ClassicLevelManager(root);
        levelManager.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
