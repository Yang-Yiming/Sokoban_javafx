package org.view.menu;

import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.Time;

public class MenuView extends AnchorPane {
    private Button startButton;
    private Button loginButton;
    private Button settingsButton;
    MenuController menuController;

    public MenuView(MenuController menuController){
        this.menuController = menuController;
        setPrefHeight(600.0);
        setPrefWidth(800.0);

        ImageView title = new ImageView(new Image(getClass().getResourceAsStream("/images/title.png"))); //有 gif 了
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
        // 太可爱了建议保留
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                    new KeyValue(title.rotateProperty(), 0),
                    new KeyValue(title.fitWidthProperty(), 1 * title.getFitWidth()),
                    new KeyValue(title.fitHeightProperty() , 1 * title.getFitHeight())),
            new KeyFrame(Duration.millis(5000),
                    new KeyValue(title.rotateProperty(), -4),
                    new KeyValue(title.fitWidthProperty(), 1.2 * title.getFitWidth()),
                    new KeyValue(title.fitHeightProperty() , 1.2 * title.getFitHeight())),
            new KeyFrame(Duration.millis(10000),
                    new KeyValue(title.rotateProperty(), 0),
                    new KeyValue(title.fitWidthProperty(), 1 * title.getFitWidth()),
                    new KeyValue(title.fitHeightProperty() , 1 * title.getFitHeight())),
            new KeyFrame(Duration.millis(15000),
                    new KeyValue(title.rotateProperty(), 4),
                    new KeyValue(title.fitWidthProperty(), 1.2 * title.getFitWidth()),
                    new KeyValue(title.fitHeightProperty() , 1.2 * title.getFitHeight())),
            new KeyFrame(Duration.millis(20000),
                    new KeyValue(title.rotateProperty(), 0),
                    new KeyValue(title.fitWidthProperty(), 1 * title.getFitWidth()),
                    new KeyValue(title.fitHeightProperty() , 1 * title.getFitHeight()))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        startButton = generate_button("开始", 315, 310);
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

    private Button generate_button(String text, double x, double y) {
        Button btn = new Button(text);
        btn.setLayoutX(x - btn.getPrefWidth() / 2);
        btn.setLayoutY(y - btn.getPrefHeight() / 2);
        btn.setPrefHeight(57.0);
        // btn.setPrefWidth(161.0);
        btn.setFont(new Font(30.0));
        btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px;");
        btn.setOnMouseEntered(event -> btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px; -fx-font-size: 35px;"));
        btn.setOnMouseExited(event -> btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px; -fx-font-size: 30px;"));
        return btn;
    }

    private void startButtonClicked() {
        // 按钮生成
        Button btn_mode1 = generate_button("经典模式", 315, 270);
        Button btn_mode2 = generate_button("无尽模式", 315, 320);
        Button btn_mode3 = generate_button("双人模式", 315, 370);
        getChildren().addAll(btn_mode1, btn_mode2, btn_mode3);

        // 动画
        FadeTransition btn1_transition = generate_fade_transition(btn_mode1,0.2, 0.0, 1.0);
        FadeTransition btn2_transition = generate_fade_transition(btn_mode2,0.2, 0.0, 1.0);
        FadeTransition btn3_transition = generate_fade_transition(btn_mode3,0.2, 0.0, 1.0);

        FadeTransition fadeTransition = generate_fade_transition(startButton,0.2, 1.0, 0.0);
        fadeTransition.setOnFinished(event -> {
            getChildren().remove(startButton);
            btn1_transition.play();
            btn2_transition.play();
            btn3_transition.play();
        });
        // Handle start button click
        btn_mode1.setOnMouseClicked(event -> menuController.StartButtonClicked());
        //menuController.StartButtonClicked();
    }

    private FadeTransition generate_fade_transition(Button button, double duration, double from, double to) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), button);
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        fadeTransition.play();
        return fadeTransition;
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