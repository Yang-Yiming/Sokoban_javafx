package org.view.menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.view.level.ClassicLevelManager;

import java.io.IOException;

public class MenuController {

    private Pane root;
    private Stage primaryStage;
    private Scene scene;

    public MenuController() throws IOException {
    }

    public void initialize(Pane root, Stage primaryStage) {
        this.root = root;
        this.primaryStage = primaryStage;
    }

    @FXML
    private Button Login;

    @FXML
    private Button Settings;

    @FXML
    private Button StartButton;

    @FXML
    void LoginButtonClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Pane loginroot = loader.load();
        LoginController controller = loader.getController(); controller.initialize(primaryStage);
        if(scene == null) scene = new Scene(loginroot);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    void SettingButtonClicked(MouseEvent event) {

    }

    @FXML
    void StartButtonClicked(MouseEvent event) {
        ClassicLevelManager levelManager = new ClassicLevelManager(root);
        levelManager.start(primaryStage);
    }

}
