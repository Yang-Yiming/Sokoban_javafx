package org.view.LevelSelect;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.view.menu.MenuController;

import java.io.IOException;

import static javafx.application.Application.launch;

public class testMain extends Application{
    @Override
    public void start(Stage primaryStage) throws IOException {
        Pane root = new Pane();
        map map = new map(root);

        map.random_generate(4,5);

        map.draw_map();
        primaryStage.setTitle("test");
        primaryStage.setScene(map.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
