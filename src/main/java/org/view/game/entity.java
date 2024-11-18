package org.view.game;

import javafx.animation.TranslateTransition;
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
            TranslateTransition transition = new TranslateTransition(Duration.millis(config.move_anim_duration * 1000), imageView);
            transition.setFromX((x - 1) * config.tile_size); // 初始化的时候已经+了一次config.tile_size所以这里要-1
            transition.setFromY((y - 1) * config.tile_size);

            map.remove(x, y, type);
            x += velocity_x;
            y += velocity_y;
            map.add(x, y, type); // 向那一格第i位加入

            transition.setToX((x - 1) * config.tile_size);
            transition.setToY((y - 1) * config.tile_size);
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
