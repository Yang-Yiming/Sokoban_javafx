package org.view.level;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;

import javafx.stage.Stage;
import org.model.MapMatrix;
import org.model.config;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
            if(code == KeyCode.UP || code == KeyCode.W) level.player.set_velocity(0,-1);
            if(code == KeyCode.DOWN || code == KeyCode.S) level.player.set_velocity(0, 1);
            if(code == KeyCode.LEFT || code == KeyCode.A) level.player.set_velocity(-1, 0);
            if(code == KeyCode.RIGHT || code == KeyCode.D) level.player.set_velocity(1, 0);
            if(code == KeyCode.R){
                level.init();
                // System.out.println("reset");
            }
            level.player.move(level.map, level.boxes);

            level.drawMap();

            if(level.isWin()){
                ++currentLevel;
                if(currentLevel == mapdata.maps.length) currentLevel = 0;
                loadLevel();
            }
        });


        // 根据鼠标拖动改变anchor_posx anchor_posy
        // 鼠标是否在拖动
        AtomicBoolean isDragging = new AtomicBoolean(false);
        AtomicReference<Double> del_posx = new AtomicReference<>((double) 0);
        AtomicReference<Double> del_posy = new AtomicReference<>((double) 0);

        // 添加鼠标按下事件监听器
        scene.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                del_posx.set(level.getanchor_posx() - event.getSceneX());
                del_posy.set(level.getanchor_posy() - event.getSceneY());
            }
        });

        scene.setOnMouseDragged(event -> {
            level.setAnchor_posx(event.getSceneX() + del_posx.get());
            level.setAnchor_posy(event.getSceneY() + del_posy.get());

            level.drawMap();
        });


    }

    public void start(Stage primaryStage){
        this.root = new Pane();
        scene = new Scene(root, config.ScreenWidth, config.ScreenHeight);
        primaryStage.setTitle("Sokoban Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        loadLevel();
    }

}
