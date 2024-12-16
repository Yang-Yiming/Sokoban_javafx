package org.view.DifficultMode;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.data.mapdata;
import org.model.User;

import java.util.Stack;

public class DifficultMode {
    public static Font pixelFont(int size) {
        return Font.loadFont(DifficultMode.class.getResource("/font/pixel.ttf").toExternalForm(), size);
    }

    Pane root;
    StackPane stack;
    Rectangle rect;
    public static Label label;
    public static boolean opened;
    boolean isMoving;
    GridPane gridPane;
    Timeline gridFade;

    User user;

    public static int difficulty = 0;
    public static button lower_step_limit1;
    public static button lower_step_limit2;
    public static button lower_step_limit3;
    public static button no_items;
    public static button thunder;
    public static button mushrooms;
    public DifficultMode(Pane root) {
        this.root = root;

        rect = new Rectangle(0, 0, 80, 34);
//        rect.setFill(Color.rgb(112,40,30));
        rect.setFill(Color.BLACK);
        rect.setOpacity(0.3);
        // 圆角
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        rect.setStroke(Color.web("#55371d"));
        rect.setStrokeWidth(3);

        label = new Label(difficulty + "");
        label.setFont(pixelFont(30));
        label.setTextFill(Color.WHITE);
        stack = new StackPane(rect, label);

        StackPane.setAlignment(rect, Pos.TOP_LEFT);
        StackPane.setAlignment(label, Pos.BOTTOM_LEFT);
        StackPane.setMargin(label, new javafx.geometry.Insets(2, 0, 14, 37));
        opened = false;
        isMoving = false;

        stack.setOnMouseEntered(event -> {
            if(!opened && !isMoving) {
                opened = true;
                show_select_page();
            }
        });
        stack.setOnMouseExited(event -> {
            if(opened && !isMoving) {
                opened = false;
                close_select_page();
            }
        });


        // 按钮
        if(lower_step_limit1 == null) lower_step_limit1 = new button("/images/item/down.png", 1, "步数限制-", "step1");
        if(lower_step_limit2 == null) lower_step_limit2 = new button("/images/item/down2.png", 2, "步数限制--", "step2");
        if(lower_step_limit3 == null) lower_step_limit3 = new button("/images/item/down3.png", 3, "步数限制---", "step3");
        if(no_items == null) no_items = new button("/images/item/X.png", 2, "无道具", "no_items");
        if(thunder == null) thunder = new button("/images/item/cloud.png", 3, "雷雨天", "thunder");
        if(mushrooms == null) mushrooms = new button("/images/item/mushroom.png", 3, "吃菌子了", "mushrooms");

        gridPane = new GridPane();
        gridPane.setTranslateX(30);
        gridPane.setTranslateY(30);
        gridPane.setVgap(20);
        gridPane.setHgap(30);
        gridPane.add(lower_step_limit1.stack, 0, 0);
        gridPane.add(lower_step_limit2.stack, 1, 0);
        gridPane.add(lower_step_limit3.stack, 2, 0);
        gridPane.add(no_items.stack, 1, 1);
        gridPane.add(thunder.stack, 2, 1);
        gridPane.add(mushrooms.stack, 2, 2);
    }

    public static final int beginX = 10, beginY = 30;
    public void draw() {
        stack.setLayoutX(beginX);
        stack.setLayoutY(beginY);
        root.getChildren().add(stack);
    }

    void show_select_page() {
        isMoving = true;

        gridFade = new Timeline(
                new KeyFrame(Duration.ZERO, new javafx.animation.KeyValue(gridPane.opacityProperty(), 0)),
                new KeyFrame(Duration.millis(100), new javafx.animation.KeyValue(gridPane.opacityProperty(), 1))
        );

        Timeline bigger_rect = new Timeline();
        bigger_rect.getKeyFrames().add(
                new KeyFrame(
                        Duration.millis(20),
                        event -> {
                            if(rect.getWidth() < 340) {
                                rect.setWidth((rect.getWidth() + 35));
                            }
                            if(rect.getHeight() < 340) {
                                rect.setHeight((rect.getHeight() + 15));
                            } else {
                                bigger_rect.stop();
                                isMoving = false;
                                gridPane.setOpacity(0);
                                stack.getChildren().add(gridPane);
                                gridFade.play();
                            }
                        }
                )
        );
        bigger_rect.setCycleCount(Timeline.INDEFINITE);
        bigger_rect.play();
    }

    void close_select_page() {
        isMoving = true;
        gridFade = new Timeline(
                new KeyFrame(Duration.ZERO, new javafx.animation.KeyValue(gridPane.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(100), new javafx.animation.KeyValue(gridPane.opacityProperty(), 0))
        ); gridFade.play();

        Timeline smaller_rect = new Timeline();
        smaller_rect.getKeyFrames().add(
                new KeyFrame(
                        Duration.millis(20),
                        event -> {
                            if(rect.getWidth() > 80) {
                                rect.setWidth((rect.getWidth() - 35));
                            }
                            if(rect.getHeight() > 34) {
                                rect.setHeight((rect.getHeight() - 15));
                            } else {
                                smaller_rect.stop();
                                isMoving = false;
                            }

                        }
                )
        );
        smaller_rect.setCycleCount(Timeline.INDEFINITE);

        gridFade.setOnFinished(event -> {
            stack.getChildren().remove(gridPane);
            smaller_rect.play();
        });
    }
}
