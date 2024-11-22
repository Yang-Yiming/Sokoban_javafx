package org.view.LevelSelect;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class testMain extends Application{
    @Override
    public void start(Stage primaryStage) throws IOException {
        Pane root = new Pane();
        map map = new map(root);

        //map.random_generate_map(4,5);
        map.linear_generate_map(5);

        map.draw_nodes(false);
        primaryStage.setTitle("test");
        primaryStage.setScene(map.getScene());
        primaryStage.show();
        map.draw_line(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}