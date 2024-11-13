package org.view.level;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;

import javafx.stage.Stage;
import org.model.MapMatrix;
import org.view.game.box;

public class ClassicLevelManager {
    private int currentLevel;
    private MapMatrix map;
    private Pane root;
    private Scene scene;

    public ClassicLevelManager(Pane root){
        this.root = root;
        this.currentLevel = 0;
        this.map = new MapMatrix(mapdata.maps[currentLevel]);

    }

    public void loadLevel(){
        Level level = new Level(root, currentLevel);
        // 添加键盘监听功能
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            level.player.set_velocity(0, 0);
            if(code == KeyCode.UP) level.player.set_velocity(0,-1);
            if(code == KeyCode.DOWN) level.player.set_velocity(0, 1);
            if(code == KeyCode.LEFT) level.player.set_velocity(-1, 0);
            if(code == KeyCode.RIGHT) level.player.set_velocity(1, 0);
            if(code == KeyCode.R){
                level.init();
                // System.out.println("reset");
            }
            level.player.move(level.map, level.boxes, level.boxMatrix);

            if(level.isWin()){
                ++currentLevel;
                if(currentLevel == mapdata.maps.length) currentLevel = 0;
                loadLevel();
            }
            level.drawMap();
        });
    }

    public void start(Stage primaryStage){
        this.root = new Pane();
        scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Sokoban Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        loadLevel();
    }

}
