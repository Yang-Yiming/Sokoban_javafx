package org.view.level;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.data.mapdata;
import org.model.config;
import org.view.menu.Settings;

import java.io.FileNotFoundException;

public class FightLevelManager {
    private Pane root;
    private Stage primaryStage;
    private Scene scene;

    public FightLevelManager(Stage primaryStage) {
        this.root = new Pane();
        this.primaryStage = primaryStage;
        scene = primaryStage.getScene();
        scene.setRoot(root); // 这样应该就算是一个完全新的scene了吧
    }
    Pane levelRoot;
    FightLevel level;
    public void start() {
        //背景颜色
        root.setStyle("-fx-background-color: #8e804b");
        //添加开始游戏按钮
        Button startButton = new Button("不知道该做什么先放个 button");
        startButton.setLayoutX(100);
        startButton.setLayoutY(100);
        //取消 startButton 对上下左右键的监听
        startButton.setFocusTraversable(false);
        startButton.setOnAction(event -> {
            config.tile_size = 48;
            if(levelRoot == null) levelRoot = new Pane();
            if(level == null) level = new FightLevel(levelRoot, 1, primaryStage, null, true);
            //在 0-4 之间随机选择一个地图
            int id = (int) (Math.random() * 5);
            level.setId(id);
            level.init();
            root.getChildren().add(levelRoot);
            inLevel(level);
        });
        root.getChildren().add(startButton);
    }

    Button settingsButton;
    public void createSettingsButton(){
        Settings settings = new Settings();
        settingsButton = settings.createButton(root);
    }
    private void inLevel(FightLevel level) {
        createSettingsButton();
        root.getChildren().add(settingsButton);
        // 添加键盘监听功能
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
//             System.out.println(code);
            level.player1.set_velocity(0, 0);
            level.player2.set_velocity(0, 0);
            int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
            if (code == KeyCode.W) {
                dy1 = -1;
                level.player1.setOrientation(1);
            } else if (code == KeyCode.S) {
                dy1 = 1;
                level.player1.setOrientation(2);
            } else if (code == KeyCode.A) {
                dx1 = -1;
                level.player1.setOrientation(3);
            } else if (code == KeyCode.D) {
                dx1 = 1;
                level.player1.setOrientation(4);
            } else if (code == KeyCode.UP) {
                dy2 = -1;
                level.player2.setOrientation(1);
            } else if (code == KeyCode.DOWN) {
                dy2 = 1;
                level.player2.setOrientation(2);
            } else if (code == KeyCode.LEFT) {
                dx2 = -1;
                level.player2.setOrientation(3);
            } else if (code == KeyCode.RIGHT) {
                dx2 = 1;
                level.player2.setOrientation(4);
            } else return;
            keyPressedEvent(dx1, dy1, dx2, dy2, level);
        });
    }

    public void keyPressedEvent(int dx1, int dy1, int dx2, int dy2, FightLevel level) {
        level.player1.set_velocity(dx1, dy1);
        level.player2.set_velocity(dx2, dy2);
//        if(!level.player1.is_moving){
            level.player1.move(level.getMap(), level.boxes, level);
            level.player1.setImageTowards(level.player1.getOrientation());
//        }
//        if(!level.player2.is_moving){
            level.player2.move(level.getMap(), level.boxes, level);
            level.player2.setImageTowards(level.player2.getOrientation());
//        }
        level.drawMap();
        int whoWin = level.oneIsWin();
        if (whoWin != 0) {
            level.stopTimelines();
            //移除监听器
            scene.setOnKeyPressed(null);
            //让胜利的文字显示在屏幕中间
            Text winText = new Text();
            winText.setText("Player " + whoWin + " Win!");
            winText.setLayoutX(primaryStage.getWidth() / 2 - 100);
            winText.setLayoutY(primaryStage.getHeight() / 2);
            root.getChildren().add(winText);
            //再来一局按钮
            Button restartButton = new Button("再来一局");
            restartButton.setLayoutX(primaryStage.getWidth() / 2 - 50);
            restartButton.setLayoutY(primaryStage.getHeight() / 2 + 50);
            //返回按钮
            Button backButton = new Button("返回");
            backButton.setLayoutX(primaryStage.getWidth() / 2 - 50);
            backButton.setLayoutY(primaryStage.getHeight() / 2 + 100);
            backButton.setOnAction(event -> {
                root.getChildren().remove(winText);
                root.getChildren().remove(restartButton);
                root.getChildren().remove(backButton);
                root.getChildren().remove(levelRoot);
                level.root.getChildren().clear();
                start();
            });
            restartButton.setOnAction(event -> {
                root.getChildren().remove(winText);
                root.getChildren().remove(restartButton);
                root.getChildren().remove(backButton);
                inLevel(level);
                level.setId((int) (Math.random() * 5));
                level.init();
            });
            root.getChildren().add(restartButton);
            root.getChildren().add(backButton);
        }
    }
}

