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

        map.random_generate_map(6,4);
        //map.linear_generate_map();

        map.draw_map(false);
        primaryStage.setTitle("test");
        primaryStage.setScene(map.getScene());
        primaryStage.show();
        map.draw_line(false);
        map.update(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
