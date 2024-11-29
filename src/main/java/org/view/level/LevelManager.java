package org.view.level;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.data.mapdata;
import org.model.SavingManager;
import org.model.User;
import org.model.config;
import org.view.LevelSelect.map;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.view.LevelSelect.node;

public class LevelManager {
    private int currentLevel;
    private int totalLevel;
    private Pane root;
    private Scene scene;
    private Stage primaryStage; // 直接作为属性 不然函数里有一坨（
    private map level_menu; // 选关界面
    private int move_count;
    private Level level;

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
        level = new Level(rootLevel, currentLevel, primaryStage, user);
        scene = primaryStage.getScene();
        scene.setRoot(root); // 这样应该就算是一个完全新的scene了吧

        InLevel(id);
    }
    public void loadLevel(int id, int[][] mapmatrix) {
        root.getChildren().clear();
        currentLevel = id;
        Pane rootLevel = new Pane();
        root.getChildren().add(rootLevel);
        level = new Level(rootLevel, mapmatrix, primaryStage, currentLevel, user);
        scene = primaryStage.getScene();
        scene.setRoot(root); // 这样应该就算是一个完全新的scene了吧

        InLevel(id);
    }

    private void InLevel(int id) {
        // 添加键盘监听功能
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            level.player.set_velocity(0, 0);
            int dx = 0, dy = 0;
            if(code == KeyCode.UP || code == KeyCode.W) {dy = -1; level.player.setOrientation(1); user.addMoveCount();}
            if(code == KeyCode.DOWN || code == KeyCode.S && !event.isControlDown()) {dy = 1; level.player.setOrientation(2); user.addMoveCount();}
            if(code == KeyCode.LEFT || code == KeyCode.A) { dx = -1; level.player.setOrientation(3); user.addMoveCount();}
            if(code == KeyCode.RIGHT || code == KeyCode.D) { dx = 1; level.player.setOrientation(4); user.addMoveCount();}
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
                level.stopTimelines();
                showLevelMenu();
            }
            if(event.isControlDown() && code == KeyCode.S){ //同时按下control s时保存
                try {
                    save("保存成功");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            level.player.set_velocity(dx, dy);
            if(!level.player.is_moving){
                level.player.move(level.getMap(), level.boxes, level);
                level.player.setImageTowards(level.player.getOrientation());
            }

            level.drawMap();

            if(level.isWin()){
                level.stopTimelines();
                user.setLevelAt(++currentLevel);
                user.setMoveCount(0);
                if(currentLevel == mapdata.maps.length) currentLevel = 0;

                loadLevel(id + 1);

                try {
                    save("自动保存成功");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
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
        if(user.getMoveCount() > 0) {
            Alert alert  = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("提示");
            alert.setHeaderText("检测到上次游戏未结束，是否继续？");
            alert.setContentText("选择\"是\"继续游戏，选择\"否\"从选关界面开始");
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if(result == ButtonType.OK) {
                loadLevel(user.getLevelAt(), user.getPlayingMap());
                return;
            } else {
                user.setMoveCount(0);
            }
        }

        level_menu.linear_generate_map(user);
        showLevelMenu();
        primaryStage.setTitle("Sokoban");
        primaryStage.show();
    }

    public void showLevelMenu() {
        node.clear_all_nodes();
        level_menu.linear_generate_map(user); // 太不优雅了 但我想不到其他的方法
        level_menu.draw_map(config.is_vertical);
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

    public void save(String s) throws FileNotFoundException {
        if(user.getMoveCount() > 0) {
            user.setPlayingMap(level.getMap().getMatrix());
        }
        SavingManager.save();
        save_text(s);
    }
    public void save_text(String s) {
        Label label = new Label(s);
        label.setTextFill(Color.WHITE);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-background-color: #007BFF;");

        // 添加
        VBox layout = new VBox(label);
        root.getChildren().addLast(layout);

        // 淡入淡出
        FadeTransition fadeIn = new FadeTransition(Duration.millis(config.fade_anim_duration), label);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setOnFinished(event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(config.fade_anim_duration), label);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setDelay(Duration.millis(config.save_text_maintain));
            fadeOut.setOnFinished(event2 -> root.getChildren().remove(layout));
            fadeOut.play();
        });

        fadeIn.play();

    }

}
