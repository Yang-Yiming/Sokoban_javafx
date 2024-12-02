package org.view.menu;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.Time;

public class MenuView extends AnchorPane {
    private Button startButton;
    private Button loginButton;
    private Button settingsButton;
    MenuController menuController;

    public MenuView(MenuController menuController) {
        this.menuController = menuController;
        setPrefHeight(600.0);
        setPrefWidth(800.0);

        ImageView title = new ImageView(new Image(getClass().getResourceAsStream("/images/title.png")));
        title.setLayoutX(200.0);
        title.setLayoutY(50.0);
        title.setFitHeight(200.0);
        title.setFitWidth(400.0);
        title.setPreserveRatio(true);
        double centerX = title.getLayoutX() - title.getFitWidth() / 2;
        double centerY = title.getLayoutY() - title.getFitHeight() / 2;
        title.setTranslateX(centerX);
        title.setTranslateY(centerY);
        // Add rotate animation
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                    new KeyValue(title.rotateProperty(), 0),
                    new KeyValue(title.fitWidthProperty(), 1 * title.getFitWidth()),
                    new KeyValue(title.fitHeightProperty() , 1 * title.getFitHeight())),
            new KeyFrame(Duration.millis(800),
                    new KeyValue(title.rotateProperty(), -4),
                    new KeyValue(title.fitWidthProperty(), 1.2 * title.getFitWidth()),
                    new KeyValue(title.fitHeightProperty() , 1.2 * title.getFitHeight())),
            new KeyFrame(Duration.millis(1600),
                    new KeyValue(title.rotateProperty(), 0),
                    new KeyValue(title.fitWidthProperty(), 1 * title.getFitWidth()),
                    new KeyValue(title.fitHeightProperty() , 1 * title.getFitHeight())),
            new KeyFrame(Duration.millis(2400),
                    new KeyValue(title.rotateProperty(), 4),
                    new KeyValue(title.fitWidthProperty(), 1.2 * title.getFitWidth()),
                    new KeyValue(title.fitHeightProperty() , 1.2 * title.getFitHeight())),
            new KeyFrame(Duration.millis(3200),
                    new KeyValue(title.rotateProperty(), 0),
                    new KeyValue(title.fitWidthProperty(), 1 * title.getFitWidth()),
                    new KeyValue(title.fitHeightProperty() , 1 * title.getFitHeight()))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        startButton = new Button("Start");
        startButton.setLayoutX(315.0);
        startButton.setLayoutY(310.0);
        startButton.setPrefHeight(57.0);
        startButton.setPrefWidth(161.0);
        startButton.setFont(new Font(30.0));
        startButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px;");
        startButton.setOnMouseEntered(event -> startButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px; -fx-font-size: 35px;"));
        startButton.setOnMouseExited(event -> startButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px; -fx-font-size: 30px;"));
        startButton.setOnMouseClicked(event -> startButtonClicked());

        loginButton = new Button("Login");
        loginButton.setLayoutX(678.0);
        loginButton.setLayoutY(35.0);
        loginButton.setPrefHeight(47.0);
        loginButton.setPrefWidth(100.0);
        loginButton.setOnMouseClicked(event -> loginButtonClicked());

        settingsButton = new Button("Settings");
        settingsButton.setLayoutX(788.0);
        settingsButton.setLayoutY(35.0);
        settingsButton.setPrefHeight(47.0);
        settingsButton.setPrefWidth(100.0);
        settingsButton.setOnMouseClicked(event -> settingsButtonClicked());

        getChildren().addAll(title, startButton, loginButton, settingsButton);
    }

    private void startButtonClicked() {
        // Handle start button click
        menuController.StartButtonClicked();
    }

    private void loginButtonClicked() {
        // Handle login button click
        menuController.LoginButtonClicked();
    }

    private void settingsButtonClicked() {
        // Handle settings button click
        menuController.SettingButtonClicked();
    }
}