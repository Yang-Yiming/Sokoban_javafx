package org.view.level;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.model.config;

import java.util.concurrent.atomic.AtomicBoolean;

public class Win {
    private double ScreenWidth = config.ScreenWidth;
    private double ScreenHeight = config.ScreenHeight;

    public SequentialTransition sequentialTransition;

    private Stage stage;
    private Pane root;
    private Scene scene;

    public void get_size(){
        ScreenWidth = stage.getWidth();
        ScreenHeight = stage.getHeight();
    }

    Font pixelFont = Font.loadFont(getClass().getResource("/font/pixel.ttf").toExternalForm(), 90);
    public Font pixelFont(int size){
        return Font.loadFont(getClass().getResource("/font/pixel.ttf").toExternalForm(), size);
    }

    public void win() {
        get_size();

        // 两条线
        Line topLine = new Line(0, 0, ScreenWidth, 0);
        topLine.setStroke(Color.WHITE);
        topLine.setStrokeWidth(config.Win_Rect_Stroke);

        Line bottomLine = new Line(0, config.Win_Rect_Height, ScreenWidth, config.Win_Rect_Height);
        bottomLine.setStroke(Color.WHITE);
        bottomLine.setStrokeWidth(config.Win_Rect_Stroke);

        Label label = new Label("完成");
//        label.setStyle("-fx-font-size: 90px; -fx-text-fill: white; -fx-font-style: italic;");
        label.setFont(pixelFont);
        label.setTextFill(Color.WHITE);

        // 组合
        VBox vbox = new VBox(topLine, label, bottomLine);
        vbox.setSpacing(10);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        vbox.setPadding(new Insets(ScreenHeight / 2 - 110,0,0,0));

        root.getChildren().add(vbox);

        // 从左边出现
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(config.win_in_duration), vbox);
        translateTransition.setFromX(-ScreenWidth);
        translateTransition.setToX(0);
        translateTransition.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1));

        // 等待2秒
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(config.win_pause_duration));

        // 从右边退场
        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(config.win_in_duration), vbox);
        translateTransition2.setFromX(0);
        translateTransition2.setToX(ScreenWidth);
        translateTransition2.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1));

        // 动画序列
        sequentialTransition = new SequentialTransition(translateTransition, pauseTransition, translateTransition2);
        sequentialTransition.setOnFinished(event -> {
            root.getChildren().remove(vbox);
        });
        sequentialTransition.play();
    }

    public Win(Stage stage, Pane root) {
        this.stage = stage;
        this.root = root;
        scene = stage.getScene();
    }

    public void lose() {

        get_size();

        Rectangle rectangle = new Rectangle(0, 0, ScreenWidth, ScreenHeight);

        Label label = new Label("菜");
        label.setFont(pixelFont);
        label.setTextFill(Color.WHITE);

        Label small_label = new Label("有的猫活着");
        small_label.setFont(pixelFont(30));
        small_label.setTextFill(Color.WHITE);

        VBox vbox = new VBox(label, small_label);
        vbox.setSpacing(10);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        vbox.setPadding(new Insets(ScreenHeight / 2 - 110,0,0,ScreenWidth/2-80));
        vbox.setOpacity(0);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(400), rectangle);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(0.5);
        fadeTransition.setOnFinished(event -> {
            FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(800), vbox);
            fadeTransition2.setFromValue(0);
            fadeTransition2.setToValue(1);
            fadeTransition2.play();
            fadeTransition2.setOnFinished(event2 -> {
                KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.R, false, false, false, false);
                root.fireEvent(keyEvent);
                root.getChildren().removeAll(rectangle, vbox);
            });
        });

        fadeTransition.play();
        root.getChildren().add(rectangle);
        root.getChildren().add(vbox);
    }
}
