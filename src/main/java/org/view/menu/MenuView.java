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
import org.model.config;

import java.lang.reflect.Parameter;
import java.sql.ParameterMetaData;
import java.sql.Time;

public class MenuView extends AnchorPane {
    private Button startButton;
    private Button loginButton;
    private Button settingsButton;
    MenuController menuController;
    ImageView title = null;

    private void createTitle(){
        title = new ImageView(new Image(getClass().getResourceAsStream("/images/title.gif"))); //有 gif 了
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
//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.ZERO,
//                        new KeyValue(title.rotateProperty(), 0),
//                        new KeyValue(title.fitWidthProperty(), 1 * title.getFitWidth()),
//                        new KeyValue(title.fitHeightProperty() , 1 * title.getFitHeight())),
//                new KeyFrame(Duration.millis(5000),
//                        new KeyValue(title.rotateProperty(), -4),
//                        new KeyValue(title.fitWidthProperty(), 1.2 * title.getFitWidth()),
//                        new KeyValue(title.fitHeightProperty() , 1.2 * title.getFitHeight())),
//                new KeyFrame(Duration.millis(10000),
//                        new KeyValue(title.rotateProperty(), 0),
//                        new KeyValue(title.fitWidthProperty(), 1 * title.getFitWidth()),
//                        new KeyValue(title.fitHeightProperty() , 1 * title.getFitHeight())),
//                new KeyFrame(Duration.millis(15000),
//                        new KeyValue(title.rotateProperty(), 4),
//                        new KeyValue(title.fitWidthProperty(), 1.2 * title.getFitWidth()),
//                        new KeyValue(title.fitHeightProperty() , 1.2 * title.getFitHeight())),
//                new KeyFrame(Duration.millis(20000),
//                        new KeyValue(title.rotateProperty(), 0),
//                        new KeyValue(title.fitWidthProperty(), 1 * title.getFitWidth()),
//                        new KeyValue(title.fitHeightProperty() , 1 * title.getFitHeight()))
//        );
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();

    }

    private void createButtons(){

        startButton = generate_button("开始", (getPrefWidth() - 100) / 2, 310);
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


    }
    private void createClouds(){
        // 云朵
        ImageView cloud1 = new ImageView(new Image(getClass().getResourceAsStream("/images/clouds/Clouds1.png")));
        cloud1.setFitHeight(50.0);
        cloud1.setFitWidth(100.0);
        cloud1.setLayoutX(100.0);
        cloud1.setLayoutY(100.0);
        cloud1.setPreserveRatio(true);
        getChildren().add(cloud1);
        TranslateTransition cloud1_transition = new TranslateTransition(Duration.seconds(10), cloud1);
        cloud1_transition.setByX(800.0);
        cloud1_transition.setCycleCount(TranslateTransition.INDEFINITE);
        cloud1_transition.setAutoReverse(true);
        cloud1_transition.play();

        ImageView cloud2 = new ImageView(new Image(getClass().getResourceAsStream("/images/clouds/Clouds2.png")));
        cloud2.setFitHeight(50.0);
        cloud2.setFitWidth(100.0);
        cloud2.setLayoutX(200.0);
        cloud2.setLayoutY(150.0);
        cloud2.setPreserveRatio(true);
        getChildren().add(cloud2);
        TranslateTransition cloud2_transition = new TranslateTransition(Duration.seconds(10), cloud2);
        cloud2_transition.setByX(800.0);
        cloud2_transition.setCycleCount(TranslateTransition.INDEFINITE);
        cloud2_transition.setAutoReverse(true);
        cloud2_transition.play();

        ImageView cloud3 = new ImageView(new Image(getClass().getResourceAsStream("/images/clouds/Clouds3.png")));
        cloud3.setFitHeight(50.0);
        cloud3.setFitWidth(100.0);
        cloud3.setLayoutX(300.0);
        cloud3.setLayoutY(200.0);
        cloud3.setPreserveRatio(true);
        getChildren().add(cloud3);
        TranslateTransition cloud3_transition = new TranslateTransition(Duration.seconds(10), cloud3);
        cloud3_transition.setByX(800.0);
        cloud3_transition.setCycleCount(TranslateTransition.INDEFINITE);
        cloud3_transition.setAutoReverse(true);
        cloud3_transition.play();
    }
    private ImageView grass;
    public MenuView(MenuController menuController){
        this.menuController = menuController;
        setPrefHeight(600.0);
        setPrefWidth(800.0);
        //将背景设置为上蓝下白的渐变
        setStyle("-fx-background-color: linear-gradient(to bottom, #add8e6, #ffffff);");
        //在最下方铺一层绿色的草地
        grass = new ImageView(new Image(getClass().getResourceAsStream("/images/wall.png"))); //回头再说
        grass.setFitHeight(200.0);
        getChildren().add(grass);

        createClouds();

        createTitle();
        addScreenSizeListener();
        createButtons();
        changePosX(getWidth());
        changePosY(getHeight());
        getChildren().addAll(title, startButton, loginButton, settingsButton);
    }

    private void addScreenSizeListener(){
        widthProperty().addListener((observable, oldValue, newValue) -> {
            changePosX(newValue.doubleValue());
            changeBtnPosX(newValue.doubleValue());
        });
        heightProperty().addListener((observable, oldValue, newValue) -> {
            changePosY(newValue.doubleValue());
        });
    }
    void changePosX(double doubleValue){
        //将 title 居中靠上
        title.setLayoutX((doubleValue - title.getFitWidth()) / 2);
        //将 btn1,2,3 居中
        startButton.setLayoutX((doubleValue - 100) / 2);
        loginButton.setLayoutX(doubleValue - 200);
        settingsButton.setLayoutX(doubleValue - 100);
        grass.setFitWidth(doubleValue);
    }
    void changePosY(double doubleValue){
        grass.setLayoutY(doubleValue - 100);
    }
    void changeBtnPosX(double doubleValue){
        btn_mode1.setLayoutX((doubleValue - 180) / 2);
        btn_mode2.setLayoutX((doubleValue - 180) / 2);
        btn_mode3.setLayoutX((doubleValue - 180) / 2);
    }

    private ScaleTransition createScaleTransition(Button button, double size1, double size2, Duration duration) {
        ScaleTransition scaleTransition = new ScaleTransition(duration, button);
        scaleTransition.setFromX(size1);
        scaleTransition.setFromY(size1);
        scaleTransition.setToX(size2);
        scaleTransition.setToY(size2);
        scaleTransition.setCycleCount(1);
        scaleTransition.setAutoReverse(true); // 动画结束后自动反向播放
        return scaleTransition;
    }

    private Button generate_button(String text, double x, double y) {
        Button btn = new Button(text);
        btn.setLayoutX(x - btn.getPrefWidth() / 2);
        btn.setLayoutY(y - btn.getPrefHeight() / 2);
        btn.setPrefHeight(57.0);
        // btn.setPrefWidth(161.0);
        btn.setFont(new Font(30.0));
        btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px;");
        ScaleTransition scaleUp = createScaleTransition(btn, 1, config.font_size_2 / config.font_size_1, Duration.millis(config.font_size_change_millis));
        ScaleTransition scaleDown = createScaleTransition(btn, config.font_size_2 / config.font_size_1, 1, Duration.millis(config.font_size_change_millis));

        btn.setOnMouseEntered(event -> scaleUp.play());

        // 鼠标离开事件
        btn.setOnMouseExited(event -> scaleDown.play());

        return btn;
    }
    Button btn_mode1, btn_mode2, btn_mode3;
    private void startButtonClicked() {
        // 按钮生成
        btn_mode1 = generate_button("经典模式", 300, 270);
        btn_mode2 = generate_button("无尽模式", 300, 320);
        btn_mode3 = generate_button("双人模式", 300, 370);
        changeBtnPosX(getWidth());
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
        btn_mode2.setOnMouseClicked(event -> menuController.startInfiniteLevel());
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