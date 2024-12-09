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
        home.setLayoutX(345);
        home.setLayoutY(270);
        home.setPrefSize(100, 50);
        home.setStyle("-fx-background-color: #55371d; -fx-border-color: #55371d; -fx-border-width: 2px; -fx-text-fill: #f2f2f2;");
        home.setFont(new Font(pixelFont.getName(), 20));
        root.getChildren().add(home);
        home.setOnMouseClicked(event -> {
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
}