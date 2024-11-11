package view.game;

import javafx.scene.image.Image;
import model.MapMatrix;

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
        return mapMatrix.get_objects_id(x + velocity_x, y + velocity_y).isEmpty(); // 什么都没有才可以动
    }

    public void move(MapMatrix mapMatrix) {
        if(can_move(mapMatrix)){
            x += velocity_x;
            y += velocity_y;
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
