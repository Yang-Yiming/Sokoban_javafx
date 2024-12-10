package org.view.LevelSelect;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.model.config;
import org.view.level.LevelManager;

public class MapNode {
    public static LevelManager levelManager;

    private Stage stage;
    private Scene scene;
    private Pane root;
    private Button button;
    private int index;
    private boolean is_locked;
    private int target_level;
    private int[][][] maps;
    private double posX, posY;

    public MapNode(int index, Stage stage){
        this.index = index;
        this.is_locked = false;

        this.stage = levelManager.getPrimaryStage();
        this.scene = stage.getScene();
        this.button = new Button();
        button.setPrefSize(config.Map_Node_Width, config.Map_Node_Height);
        button.setOnAction(e -> action());
    }

    public void action() {
        if (!is_locked) {
            load_level();
        }
    }
    public void load_level() {
        levelManager.loadLevel(target_level, maps);
    }

    //getter
    public int getIndex() {
        return index;
    }

    //获得坐标
    public double get_left_posX() {
        return button.localToScene(scene.getX(),scene.getY()).getX();
    }
}
