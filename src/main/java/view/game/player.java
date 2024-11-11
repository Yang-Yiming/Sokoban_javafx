package view.game;

import javafx.scene.image.Image;
import model.MapMatrix;

import java.util.ArrayList;

public class player extends entity{

    public player(int x, int y) {
        super(x, y, 2);
        image = new Image("");
    }

    public void push(entity obj) {
        if(obj.can_be_moved){
            obj.velocity_x = velocity_x;
            obj.velocity_y = velocity_y;
        }
    } // 从这个角度来说 结算移动的时候应当先结算箱子再结算player？ 不然可能箱子润了人没润 但感觉一点也不优雅

    @Override
    public void move(MapMatrix mapMatrix) {
        //push()
        if(can_move(mapMatrix)){
            x += velocity_x;
            y += velocity_y;
            mapMatrix.add(x,y, (int)Math.pow(2, type)); //向那一格第i位加入
        }
    }

}
