package org.view.level;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;

import javafx.stage.Stage;
import org.model.User;
import org.model.config;
import org.view.LevelSelect.map;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.view.LevelSelect.node;

public class LevelManager {
    private int currentLevel;
    private int totalLevel;
    private Pane root;
    private Scene scene;
    private Stage primaryStage; // 直接作为属性 不然函数里有一坨（
    private map level_menu; // 选关界面

    private User user = null; // 正在游玩的user，用于存档

    public LevelManager(Stage primaryStage) {
        this.root = new Pane(); // 直接用新的Pane 除了stage其他全部新建
        this.currentLevel = 0;
        this.totalLevel = mapdata.maps.length;
        this.primaryStage = primaryStage; // 主舞台
        this.level_menu = new map(primaryStage); // 选关界面

        node.levelManager = this;
    }

    public void loadLevel(int id){
        root.getChildren().clear();
        currentLevel = id;
        Pane rootLevel = new Pane();
        root.getChildren().add(rootLevel);
        Level level = new Level(rootLevel, currentLevel, primaryStage);
        scene = primaryStage.getScene();
        scene.setRoot(root); // 这样应该就算是一个完全新的scene了吧

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
                level.stopTimelines();
                level.init();
                // System.out.println("reset");
            }
            if(code == KeyCode.ESCAPE){
                //回到关卡选择界面并移除监听和动画
                scene.setOnKeyPressed(null);
                scene.setOnMousePressed(null);
                scene.setOnMouseDragged(null);
                showLevelMenu();
            }
            level.player.set_velocity(dx, dy);
            level.player.move(level.getMap(), level.boxes, level);

            level.drawMap();

            if(level.isWin()){
                level.stopTimelines();
                ++currentLevel;
                if(currentLevel == mapdata.maps.length) currentLevel = 0;
                loadLevel(id + 1);
            }
        });

        // 根据鼠标拖动改变anchor_posx anchor_posy
        // 鼠标是否在拖动根据鼠标拖动改变anchor_posx
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
            level.player.stopCameraTimeline();
            level.setAnchor_posx(event.getSceneX() + del_posx.get());
            level.setAnchor_posy(event.getSceneY() + del_posy.get());
            level.updateAllRadiatingEffect();
            level.drawMap();
        });

        // 监听大小改变
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            // level.setAnchor_posx(level.getAnchor_posx() + (newValue.doubleValue() - oldValue.doubleValue()) / 2);
            level.getCanvas().setWidth(newValue.doubleValue());
            level.drawMap();
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            // level.setAnchor_posy(level.getAnchor_posy() + (newValue.doubleValue() - oldValue.doubleValue()) / 2);
            level.getCanvas().setHeight(newValue.doubleValue());
            level.drawMap();
        });
    }

    public void start() {
        level_menu.linear_generate_map();
        showLevelMenu();
        primaryStage.setTitle("Sokoban");
        primaryStage.show();
    }
    public void showLevelMenu() {
        level_menu.draw_map(false);
        primaryStage.setScene(level_menu.getScene());
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
