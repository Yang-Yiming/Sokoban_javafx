package view.game;

import javafx.scene.image.Image;
import model.MapMatrix;
import java.lang.Math;

// 所有会动的东西的父类
public class entity {
    protected int x;
    protected int y;
    protected int type;
    protected Image image;

    protected boolean can_be_moved = true;

    protected int velocity_x;
    protected int velocity_y;

    public entity(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    //methods
    public boolean can_move(MapMatrix mapMatrix) {
        return mapMatrix.hasNoObstacle(x + velocity_x, y + velocity_y); // 没有障碍物的时候动
    }

    public void move(MapMatrix mapMatrix) {
        if(can_move(mapMatrix)){
            mapMatrix.add(x,y, -(int)Math.pow(2, type)); //把原来第i位删掉
            x += velocity_x;
            y += velocity_y;
            mapMatrix.add(x,y, (int)Math.pow(2, type)); //向那一格第i位加入
        }
    }

    //getter
    public int get_x(){
        return x;
    }
    public int get_y(){
        return y;
    }

}
