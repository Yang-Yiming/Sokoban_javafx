package org.view.LevelSelect;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.model.config;
import org.view.level.Grass;
import org.view.level.LevelManager;

public class MapNode {

    public static final int YRange = 4;

    public static LevelManager levelManager;
    public static int[][][] maps;

    private Stage stage;
    private Scene scene;
    private Pane root;
    Button button;
    int index;
    boolean is_locked;
    int target_level;
    private double posX, posY;
    int y;

    public MapNode(int index, Stage stage){
        this.index = index;
        this.is_locked = false;

        this.stage = levelManager.getPrimaryStage();
        this.scene = stage.getScene();
        this.button = new Button();
        button.setPrefSize(config.Map_Node_Width, config.Map_Node_Height);
        button.setOnAction(e -> action());

        this.y = Grass.myRand(index, index * 31, (index - 17) * 3, -YRange, YRange);
    }

    public void action() {
        if (!is_locked) {
            SelectMap.AnchorX = 0; SelectMap.AnchorY = 0;
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
    public double get_right_posX() {
        return button.localToScene(scene.getX(),scene.getY()).getX() + config.Map_Node_Width;
    }

}
