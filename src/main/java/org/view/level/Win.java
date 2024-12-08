package org.view.level;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
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
}
