package org.view.level;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;

import javafx.stage.Stage;
import org.model.MapMatrix;
import org.model.config;
import org.view.game.box;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;



public class ClassicLevelManager {
    private int currentLevel;
    private int totalLevel;
    private MapMatrix map;
    private Pane root;
    private Scene scene;

    public ClassicLevelManager(Pane root){
        this.root = root;
        this.currentLevel = 0;
        this.totalLevel = mapdata.maps.length;
        this.map = new MapMatrix(mapdata.maps[currentLevel]);

    }

    public void loadLevel(int id){
        currentLevel = id;
        Level level = new Level(root, currentLevel);

        // 添加键盘监听功能
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            level.player.set_velocity(0, 0);
            int dx = 0, dy = 0;
            if(code == KeyCode.UP || code == KeyCode.W) dy = -1;
            if(code == KeyCode.DOWN || code == KeyCode.S) dy = 1;
            if(code == KeyCode.LEFT || code == KeyCode.A) dx = -1;
            if(code == KeyCode.RIGHT || code == KeyCode.D) dx = 1;
            if(code == KeyCode.R){
                level.init();
                // System.out.println("reset");
            }
            level.player.set_velocity(dx, dy);
            level.player.move(level.map, level.boxes);
//            if(level.player.move(level.map, level.boxes)){
//                //level.movePlayer(dx, dy);
//                for(box b : level.boxes){
////                    if(b.isMoving()) {
////                        //level.moveBox(b, dx,dy);
////                        //b.setMoving(false);
////                    }
//                }
//            };

            level.drawMap();

            if(level.isWin()){
                ++currentLevel;
                if(currentLevel == mapdata.maps.length) currentLevel = 0;
                loadLevel(id + 1);
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

        VBox vbox = new VBox(10); // 间距为10
        vbox.setAlignment(Pos.CENTER); // 居中对齐

        // 创建按钮，每个按钮对应一个关卡
        for (int i = 0; i < totalLevel; i++) {
            int levelIndex = i;
            Button btn = new Button("Level " + (levelIndex + 1));
            btn.setOnAction(event -> loadLevel(levelIndex)); // 设置按钮的事件处理
            vbox.getChildren().add(btn);
        }
        root.getChildren().add(vbox);
        primaryStage.setTitle("Sokoban Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
