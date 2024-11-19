package org.view.menu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.view.level.ClassicLevelManager;

public class MenuController {

    @FXML
    private Button StartButton;

    private Pane root;
    private Stage primaryStage;

    public void initialize(Pane root, Stage primaryStage) {
        this.root = root;
        this.primaryStage = primaryStage;
    }

    @FXML
    void Clicked(MouseEvent event) {
        System.out.println("Clicked");
        ClassicLevelManager levelManager = new ClassicLevelManager(root);
        levelManager.start(primaryStage);
    }

}
