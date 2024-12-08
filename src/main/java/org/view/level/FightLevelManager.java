package org.view.level;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.model.config;

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
    public void start(){
        //背景颜色
        root.setStyle("-fx-background-color: #8e804b");
        //添加开始游戏按钮
        Button startButton = new Button("不知道该做什么先放个 button");
        startButton.setLayoutX(100);
        startButton.setLayoutY(100);
        startButton.setOnAction(event -> {
            config.tile_size = 48;
            Pane backgroundRoot = new Pane();
            FightBackground background = new FightBackground(backgroundRoot, 4, primaryStage, null);
            background.init();
            root.getChildren().add(backgroundRoot);

            Pane leftRoot = new Pane();
            FightLevel leftLevel = new FightLevel(leftRoot, 4, primaryStage, null, true);
            leftLevel.init();
            leftRoot.setLayoutX(0);
            leftRoot.setLayoutY(0);
            leftRoot.setScaleX(-1);
            double leftRootWidth = leftRoot.getLayoutBounds().getWidth();
            leftRoot.setLayoutX(primaryStage.getWidth() / 2 - leftRootWidth + 200);

            Pane rightRoot = new Pane();
            FightLevel rightLevel = new FightLevel(rightRoot, 4, primaryStage, null, false);
            rightLevel.init();
            rightRoot.setLayoutX(primaryStage.getWidth() / 2 - 200);
            rightRoot.setLayoutY(0);
            root.getChildren().addAll(leftRoot, rightRoot);
        });
        root.getChildren().add(startButton);
    }
}
