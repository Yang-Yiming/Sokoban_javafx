package org;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.model.config;
import org.view.menu.MenuController;

import java.io.IOException;

import static javafx.application.Application.launch;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Menu.fxml"));
        Pane fxmlroot = loader.load();
        MenuController controller = loader.getController();

        Pane root = new Pane();
        controller.initialize(root, primaryStage);
        primaryStage.setScene(new Scene(fxmlroot));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
