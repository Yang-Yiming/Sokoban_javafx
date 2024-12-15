package org.view.DifficultMode;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.security.Key;

public class thunder {
    public Pane root;
    public Rectangle rect;
    public int ScreenWidth, ScreenHeight;
    public Timeline timeline;

    public thunder(Pane root) {
        this.root = root;
        rect = new Rectangle(0, 0, root.getScene().getWidth(), root.getScene().getHeight());
        rect.setFill(Color.BLACK);
        rect.setOpacity(0.2);

        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(2), event -> {
                    if(Math.random() < 0.4) {
                        lightning();
                    }
                })
        );
    }

    public void draw() {
        root.getChildren().add(rect);
        Timeline firstTimeline = new Timeline();
        firstTimeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(rect.opacityProperty(), 0)),
                new KeyFrame(Duration.millis(1000), new KeyValue(rect.opacityProperty(), 0)),
                new KeyFrame(Duration.millis(4000), new KeyValue(rect.opacityProperty(), 1))
        );
        firstTimeline.play();
        firstTimeline.setOnFinished(event -> {
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
    }

    void lightning() {
        rect.setFill(Color.rgb(255, 255, 255));
        rect.setOpacity(0.4);
        Timeline lightTimeline = new Timeline();
        lightTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(90), new KeyValue(rect.opacityProperty(), 0))
        );
        lightTimeline.play();
        lightTimeline.setOnFinished(event -> {
            rect.setFill(Color.BLACK);
            Timeline endTimeline = new Timeline();
            endTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(1000), new KeyValue(rect.opacityProperty(), 1))
            );
            endTimeline.play();
        });
    }
}
