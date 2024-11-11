// src/Main.java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage){
        Pane root = new Pane();
        LevelManager levelManager = new LevelManager(root);
        levelManager.start(primaryStage);
    }

    public static void main(String[] args){
        launch(args);
    }
}