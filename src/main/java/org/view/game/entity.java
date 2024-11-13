package org.view.game;

import javafx.scene.image.Image;
import org.model.MapMatrix;

import java.lang.Math;

// 所有会动的东西的父类
public class entity {
    protected int x;
    protected int y;
    protected int type;
    protected Image image;

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
            map.remove(x, y, type);
            x += velocity_x;
            y += velocity_y;
            map.add(x, y, type); // 向那一格第i位加入
        }
    }

    // getter
    public int get_x() {
        return x;
    }

    public int get_y() {
        return y;
    }

}
