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
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Menu.fxml"));
//        Pane fxmlroot = loader.load();
        MenuController controller = null;{
//            controller = loader.getController();
            controller = new MenuController();
        }
        try {
//            controller = loader.getController();
            controller = new MenuController();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        Pane root = new Pane();
//        primaryStage.setScene(new Scene(fxmlroot));
//        primaryStage.show();

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
