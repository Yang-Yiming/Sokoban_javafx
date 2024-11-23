package org.view.level;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;

import javafx.stage.Stage;
import org.model.User;
import org.model.config;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;



public class LevelManager {
    private int currentLevel;
    private int totalLevel;
    private Pane root;
    private Scene scene;
    private Stage primaryStage; // 直接作为属性 不然函数里有一坨（
    private VBox vbox;

    private User user = null; // 正在游玩的user，用于存档

    public LevelManager(Pane root) {
        this.root = root;
        this.currentLevel = 0;
        this.totalLevel = mapdata.maps.length;
    }

    public void loadLevel(int id){
        root.getChildren().clear();
        currentLevel = id;
        Pane rootLevel = new Pane();
        root.getChildren().add(rootLevel);
        Level level = new Level(rootLevel, currentLevel);
        scene = primaryStage.getScene();

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


    }

    public void start() {
        scene = new Scene(root, config.ScreenWidth, config.ScreenHeight);

        // 加载 CSS 文件
        scene.getStylesheets().add("file://" + new java.io.File("./src/main/resources/css/styles.css").getAbsolutePath());

        vbox = new VBox(10); // 间距为10
        vbox.setAlignment(Pos.CENTER); // 居中对齐
        // 创建按钮，每个按钮对应一个关卡
        for (int i = 0; i < totalLevel; i++) {
            int levelIndex = i;
            Button btn = new Button("Level " + (levelIndex + 1));
            btn.getStyleClass().add("button-level"); // 应用 CSS 样式
            btn.setOnAction(event -> loadLevel(levelIndex)); // 设置按钮的事件处理
            vbox.getChildren().add(btn);
        }
        primaryStage.setTitle("Sokoban Game");
        primaryStage.setScene(scene);
        showLevelMenu();
    }
    public void showLevelMenu() {
        root.getChildren().clear();
        root.getChildren().add(vbox);
        primaryStage.show();
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
