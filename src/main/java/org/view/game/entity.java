package org.view.game;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.css.SizeUnits;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.model.MapMatrix;
import org.model.config;

import java.lang.Math;

// 所有会动的东西的父类
public class entity {
    protected int x;
    protected int y;

    protected int type;
    protected Image image;
    protected ImageView imageView;

    protected double posx;
    protected double posy;

    protected int velocity_x;
    protected int velocity_y;

    public entity(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    // methods
    public boolean can_move(MapMatrix map, int vx, int vy) {
        return map.hasNoObstacle(x + vx, y + vy); // 没有障碍物的时候动
    }

    public void move(MapMatrix map) {
        if (can_move(map, velocity_x, velocity_y)) {
            // 动画
            imageView.setX(imageView.getX() + velocity_x * config.tile_size); // 更新
            imageView.setY(imageView.getY() + velocity_y * config.tile_size); // 更新
            TranslateTransition transition = new TranslateTransition(Duration.millis(config.move_anim_duration), imageView);
            transition.setFromX(-velocity_x * config.tile_size);
            transition.setFromY(-velocity_y * config.tile_size); // 同box.java

            if(x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight()) map.remove(x, y, type);
            x += velocity_x;
            y += velocity_y;
            if(x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight()) map.add(x, y, type); // 向那一格第i位加入

//            transition.setToX(velocity_x * config.tile_size);
//            transition.setToY(velocity_y * config.tile_size);
            transition.setToX(0);
            transition.setToY(0);
//            System.out.println(transition.getFromX() + " " + transition.getFromX() + " " + transition.getToX() + " " + transition.getToY());
            transition.setOnFinished(event -> {
                imageView.setTranslateX(0); // 重置 translateX，因为动画已经结束
                imageView.setTranslateY(0); // 重置 translateY，因为动画已经结束
            });
            transition.play();
        }
    }

    // getter
    public int get_x() {
        return x;
    }

    public int get_y() {
        return y;
    }
    public void set_x(int x){
        this. x = x;
    }

    public void set_y(int y){
        this.y = y;
    }
    public int getVelocity_x() {
        return velocity_x;
    }
    public int getVelocity_y() {
        return velocity_y;
    }
}
