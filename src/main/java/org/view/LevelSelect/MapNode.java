package org.view.LevelSelect;

import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.model.Rand;
import org.model.config;
import org.view.level.LevelManager;

import java.util.Random;

public class MapNode {

    public static final int YRange = 4;
    public static final int StrokeWidth = 2;

    public static LevelManager levelManager;
    public static int[][][] maps;

    private Stage stage;
    private Scene scene;
    private Pane root;
    ImageView imageView;
    int index;
    StackPane stackPane;
    boolean is_locked;
    int target_level;
    private double posX, posY;
    int x,y;

    public MapNode(int index, Stage stage, Random rand){
        this.index = index;
        this.is_locked = false;

        this.stage = levelManager.getPrimaryStage();
        this.scene = stage.getScene();

        // 创建图片
        this.imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/level.png"), config.Map_Node_Width, config.Map_Node_Width, false, false));
        imageView.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px;");
        imageView.setFitWidth(config.Map_Node_Width); imageView.setFitHeight(config.Map_Node_Width);

        // 图片上的字
        Font pixelFont = Font.loadFont(getClass().getResource("/font/pixel.ttf").toExternalForm(), 25);
        Label label = new Label(Integer.toString(index + 1));
        label.setOpacity(0.2);
        label.setFont(pixelFont);
        label.setTextFill(Color.WHITE);

//        // 发光特效
//        Glow glow = new Glow();
//        glow.setLevel(0.0);
//        button.setEffect(glow);
//        // 让glow的level随时间变化
//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.0)),
//                new KeyFrame(Duration.millis(1000), new KeyValue(glow.levelProperty(), is_locked? 0.0:0.5, Interpolator.EASE_BOTH))
//        );
//        timeline.setAutoReverse(true);
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();
        // 好像有问题 以后再改

        stackPane = new StackPane(imageView, label);

        this.imageView.setOnMouseEntered(e -> {
            if(is_locked) return;
            FadeTransition ft = new FadeTransition(Duration.millis(200), label);
            ft.setFromValue(0.2);
            ft.setToValue(0.8);
            ft.play();
        });
        this.imageView.setOnMouseExited(e -> {
            if(is_locked) return;
            FadeTransition ft = new FadeTransition(Duration.millis(200), label);
            ft.setFromValue(0.8);
            ft.setToValue(0.2);
            ft.play();
        });


        //button.setOnMouseClicked(e -> action());

        //this.y = Grass.myRand(index, index * 31, (index - 17) * 3, -YRange, YRange);
        this.x = (index % 5) * 3;
        this.y = (int)(rand.nextDouble() * YRange);
    }

    public void action() {
        if (!is_locked) {
            SelectMap.AnchorX = 0; SelectMap.AnchorY = 0;
            load_level();
        }
    }
    public void load_level() {
        levelManager.loadLevel(target_level, maps);
    }

    //getter
    public int getIndex() {
        return index;
    }

    //获得坐标
    public double get_left_posX() {
        return imageView.localToScene(scene.getX(),scene.getY()).getX();
    }
    public double get_right_posX() {
        return imageView.localToScene(scene.getX(),scene.getY()).getX() + config.Map_Node_Width;
    }

}
