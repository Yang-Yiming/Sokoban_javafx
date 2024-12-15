package org.view.DifficultMode;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.sql.Time;

public class DifficultMode {
    Font pixelFont = Font.loadFont(getClass().getResource("/font/pixel.ttf").toExternalForm(), 30);

    Pane root;
    StackPane stack;
    Rectangle rect;
    public static Label label;
    public static boolean opened;
    GridPane girdPane;
    Timeline girdFade;

    public static int difficulty = 0;
    public static boolean lower_step_limit1 = false;
    public static boolean lower_step_limit2 = false;
    public static boolean lower_step_limit3 = false;
    public static boolean no_items = false;
    public static boolean thunder = false;
    public static boolean mushrooms = false;

    public DifficultMode(Pane root) {
        this.root = root;
        rect = new Rectangle(0, 0, 80, 34);
        rect.setFill(Color.rgb(112,40,30));
        rect.setOpacity(0.8);
        // 圆角
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        rect.setStroke(Color.rgb(112,20,1));

        label = new Label(difficulty + "");
        label.setFont(pixelFont);
        label.setTextFill(Color.WHITE);
        stack = new StackPane(rect, label);
        opened = false;

        rect.setOnMouseClicked(event -> {
            opened = !opened;
            if(opened)
                close_select_page();
            else
                show_select_page();
        });

        // 按钮
        button lower_step_limit1 = new button("/images/item/down.png", 1, "步数限制-", "step1");
        button lower_step_limit2 = new button("/images/item/down2.png", 2, "步数限制--", "step2");
        button lower_step_limit3 = new button("/images/item/down3.png", 3, "步数限制---", "step3");
        button no_items = new button("/images/item/X.png", 2, "无道具", "no_items");
        button thunder = new button("/images/item/cloud.png", 3, "雷雨天", "thunder");
        button mushrooms = new button("/images/item/mushroom.png", 3, "吃菌子了", "mushrooms");

        girdPane = new GridPane();
        girdPane.setVgap(20);
        girdPane.setHgap(30);
        girdPane.add(lower_step_limit1.stack, 0, 0);
        girdPane.add(lower_step_limit2.stack, 1, 0);
        girdPane.add(lower_step_limit3.stack, 2, 0);
        girdPane.add(no_items.stack, 1, 1);
        girdPane.add(thunder.stack, 2, 1);
        girdPane.add(mushrooms.stack, 2, 2);
    }

    public void draw() {
        stack.setLayoutX(10);
        stack.setLayoutY(30);
        root.getChildren().add(stack);
    }

    void show_select_page() {
        Timeline bigger_rect = new Timeline();
        bigger_rect.getKeyFrames().add(
                new KeyFrame(
                        Duration.millis(20),
                        event -> {
                            if(rect.getWidth() < 550) {
                                rect.setWidth((rect.getWidth() + 35));
                            }
                            if(rect.getHeight() < 400) {
                                rect.setHeight((rect.getHeight() + 15));
                            } else {
                                bigger_rect.stop();
                            }
                        }
                )
        );
        bigger_rect.setCycleCount(Timeline.INDEFINITE);
        bigger_rect.play();

        girdFade = new Timeline(
                new KeyFrame(Duration.ZERO, new javafx.animation.KeyValue(girdPane.opacityProperty(), 0)),
                new KeyFrame(Duration.millis(500), new javafx.animation.KeyValue(girdPane.opacityProperty(), 0)),
                new KeyFrame(Duration.millis(1000), new javafx.animation.KeyValue(girdPane.opacityProperty(), 1))
        );
        stack.getChildren().add(girdPane);
        girdFade.play();
    }

    void close_select_page() {
        girdFade = new Timeline(
                new KeyFrame(Duration.ZERO, new javafx.animation.KeyValue(girdPane.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(200), new javafx.animation.KeyValue(girdPane.opacityProperty(), 0))
        );
        girdFade.setOnFinished(event -> {
            root.getChildren().remove(girdPane);
        });

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
                            }
                        }
                )
        );
        smaller_rect.setCycleCount(Timeline.INDEFINITE);
        smaller_rect.play();
    }
}
