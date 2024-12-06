package org.view.level;

import javafx.scene.Scene;
import javafx.scene.control.Button;
        if(haveUser()){
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
            FightLevel level = new FightLevel(root, 1, primaryStage, null);
            level.init();
        });
        root.getChildren().add(startButton);
    }
}
