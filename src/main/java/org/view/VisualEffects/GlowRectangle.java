package org.view.VisualEffects;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.model.config;

import java.util.ArrayList;

public class GlowRectangle {

    private static final double lowLimit = 0.9;
    private static final double highLimit = 1.2;

    public static ArrayList<GlowRectangle> glowRectangles = new ArrayList<>();
    public static Timeline timeline;
    public static double anchor_posx, anchor_posy;

    private int x, y;
    private Rectangle rect;

    public GlowRectangle(int x, int y) {
        this.x = x; this.y = y;
        double initialCenterX = anchor_posx + x * config.tile_size;
        double initialCenterY = anchor_posy + y * config.tile_size;
        rect = new Rectangle(initialCenterX, initialCenterY, config.tile_size * lowLimit, config.tile_size * lowLimit);
        rect.setX(initialCenterX - (rect.getWidth() - config.tile_size) / 2);
        rect.setY(initialCenterY - (rect.getHeight() - config.tile_size) / 2);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.WHITE);
        rect.setStrokeWidth(2);

        glowRectangles.add(this);

        // 动画
        if(timeline == null) {
            timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), e -> {
                for (GlowRectangle glowRect : glowRectangles) {
                    Rectangle rect = glowRect.getRect();
                    if (rect.getWidth() > config.tile_size * highLimit) {
                        rect.setWidth(config.tile_size * lowLimit);
                        rect.setHeight(config.tile_size * lowLimit);
                    }
                    // 正方形逐渐放大
                    rect.setWidth(rect.getWidth() + 1);
                    rect.setHeight(rect.getHeight() + 1);
                    // 正方形居中
                    glowRect.update(glowRect.getX(), glowRect.getY());
                    // 正方形逐渐变淡
                    double v = 1 - (rect.getWidth() - config.tile_size * lowLimit) / (config.tile_size * (highLimit - lowLimit));
                    rect.setStroke(Color.rgb(255, 255, 255, Math.max(v, 0)));
                }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
        if(timeline.getStatus() != Animation.Status.RUNNING){
            timeline.play();
        }
    }

    public void update(int x, int y) {
        double centerX = anchor_posx + x * config.tile_size;
        double centerY = anchor_posy + y * config.tile_size;
        rect.setX(centerX - (rect.getWidth() - config.tile_size) / 2);
        rect.setY(centerY - (rect.getHeight() - config.tile_size) / 2);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Rectangle getRect() {
        return rect;
    }

}
