package org.view.menu;

import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.model.config;
import org.view.level.FightLevelManager;

import java.io.IOException;
import java.sql.Time;

import static org.view.menu.MenuView.mediaPlayer;

public class Home {
    Button homeButton;
    public Button createButton(Pane root, Stage primaryStage, MenuController controller) {
        homeButton = new Button();
        ImageView homeImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/home.png")));
        homeImageView.setFitWidth(30);
        homeImageView.setFitHeight(30);
        homeButton.setGraphic(homeImageView);
        homeButton.setMaxSize(30, 30);
        homeButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px;");
        homeButton.setLayoutX(root.getWidth() - 150);
        homeButton.setLayoutY(35.0);
        homeButton.setOnMouseClicked(event -> homeButtonClicked(root, primaryStage, controller));
        //鼠标放上去时变成 home_clicked.png
        homeButton.setOnMouseEntered(event -> {
            homeImageView.setImage(new Image(getClass().getResourceAsStream("/images/home_clicked.png")));
        });
        homeButton.setOnMouseExited(event -> {
            homeImageView.setImage(new Image(getClass().getResourceAsStream("/images/home.png")));
        });
        //取消 homeButton 对上下左右键的监听
        homeButton.setFocusTraversable(false);

        //添加屏幕大小改变时的监听
        root.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(shade != null) shade.setWidth(newValue.doubleValue());
        });
        root.heightProperty().addListener((observable, oldValue, newValue) -> {
            if(shade != null) shade.setHeight(newValue.doubleValue());
        });

        return homeButton;
    }
    Rectangle shade;
    ImageView paper = new ImageView(new Image(getClass().getResourceAsStream("/images/paper.png"), 500, 500, false, false));
    Font pixelFont = Font.loadFont(getClass().getResource("/font/pixel.ttf").toExternalForm(), 30);
    private void homeButtonClicked(Pane root, Stage primaryStage, MenuController controller) {
        //变暗
        shade = new Rectangle(0, 0, root.getWidth(), root.getHeight());
        shade.setFill(Color.rgb(0, 0, 0, 0.5));
        root.getChildren().add(shade);
        //纸
        paper.setX(150);
        paper.setY(50);
        root.getChildren().add(paper);
//        标题
        Text homeText = new Text(310.0, 150.0, "返回主页");
        homeText.setFill(javafx.scene.paint.Color.web("#55371d"));
        homeText.setFont(new Font(pixelFont.getName(), 45));
        root.getChildren().add(homeText);

        //返回主页按钮
        Button home = new Button("Home");
        home.setLayoutX(348);
        home.setLayoutY(270);
        home.setPrefSize(100, 50);
        home.setStyle("-fx-background-color: #55371d; -fx-border-color: #55371d; -fx-border-width: 2px; -fx-text-fill: #f2f2f2;");
        home.setFont(new Font(pixelFont.getName(), 20));
        root.getChildren().add(home);
        home.setOnMouseClicked(event -> {
            homeAction(root, controller, primaryStage);
        });

        //关闭按钮
        Button close = new Button();
        Image X = new Image(getClass().getResourceAsStream("/images/X.png"), 30, 30, false, false);
        close.setGraphic(new ImageView(X));
        root.getChildren().add(close);
        close.setLayoutX(630);
        close.setLayoutY(60);
        close.setMaxSize(30, 30);
        close.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px; -fx-text-fill: #55371d;");
        //点击关闭按钮
        close.setOnMouseClicked(event -> {
            root.getChildren().remove(shade);
            root.getChildren().remove(paper);
            root.getChildren().remove(homeText);
            root.getChildren().remove(close);
            root.getChildren().remove(home);
        });
    }
    public void account(Pane root, Stage primaryStage, MenuController controller, long start_time, int level_past) {
        //变暗
        shade = new Rectangle(0, 0, root.getWidth(), root.getHeight());
        shade.setFill(Color.rgb(0, 0, 0, 0.5));
        root.getChildren().add(shade);
        //纸
        paper.setX(150);
        paper.setY(50);
        root.getChildren().add(paper);
//        标题
        Text homeText;
        if(level_past == 0) homeText = new Text(280, 150.0, "乐 还没过关");
        else homeText = new Text(250, 150.0, "平均用时 " + (System.currentTimeMillis() - start_time) / 1000 / level_past + " 秒");

        homeText.setFill(javafx.scene.paint.Color.web("#55371d"));
        homeText.setFont(new Font(pixelFont.getName(), 45));
        root.getChildren().add(homeText);

        //返回主页按钮
        Button home = new Button("结束");
        home.setLayoutX(348);
        home.setLayoutY(270);
        home.setPrefSize(100, 50);
        home.setStyle("-fx-background-color: #55371d; -fx-border-color: #55371d; -fx-border-width: 2px; -fx-text-fill: #f2f2f2;");
        home.setFont(new Font(pixelFont.getName(), 20));
        root.getChildren().add(home);
        home.setOnMouseClicked(event -> {
            homeAction(root, controller, primaryStage);
        });

        //关闭按钮
        Button close = new Button();
        Image X = new Image(getClass().getResourceAsStream("/images/X.png"), 30, 30, false, false);
        close.setGraphic(new ImageView(X));
//        root.getChildren().add(close);
        close.setLayoutX(630);
        close.setLayoutY(60);
        close.setMaxSize(30, 30);
        close.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px; -fx-text-fill: #55371d;");
        //点击关闭按钮
        close.setOnMouseClicked(event -> {
            root.getChildren().remove(shade);
            root.getChildren().remove(paper);
            root.getChildren().remove(homeText);
//            root.getChildren().remove(close);
            root.getChildren().remove(home);
        });
    }
    public void homeAction(Pane root, MenuController controller, Stage primaryStage) {

        //音乐停止
        mediaPlayer.stop();
        root.getChildren().clear();
        Pane menuView = new MenuView(controller);
        primaryStage.setScene(new Scene(menuView));
        primaryStage.show();
        //停止所有 timeline
        for(Timeline timeline : config.timelines){
            if(timeline != null) timeline.stop();
        }
        //停止所有 server
        if(FightLevelManager.server != null){
            //如果 server 正在运行
            if(!FightLevelManager.server.socket.isClosed()) {
                FightLevelManager.server.send(FightLevelManager.server.socket, "B");
                try {
                    FightLevelManager.server.serverSocket.close();
                    FightLevelManager.server.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //停止所有 client
        if(FightLevelManager.client != null){
            //如果 client 正在运行
            if(!FightLevelManager.client.socket.isClosed()) {
                FightLevelManager.client.send(FightLevelManager.client.socket, "B");
                try {
                    FightLevelManager.client.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
