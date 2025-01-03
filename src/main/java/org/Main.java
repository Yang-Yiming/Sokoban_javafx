package org;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.view.menu.MenuView;

import org.model.config;
import org.view.menu.MenuController;

import java.io.IOException;

import static javafx.application.Application.launch;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) throws IOException {
        config.static_seed = (int)(Math.random() * config.MAX_SEED);
        MenuController controller = null;
        try {
            controller = new MenuController();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Pane menuView = new MenuView(controller);
        Pane root = new Pane();
        controller.initialize(root, primaryStage);
        primaryStage.setScene(new Scene(menuView));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
