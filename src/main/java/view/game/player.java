package view.game;

import javafx.scene.image.Image;
import model.MapMatrix;

import java.util.ArrayList;

public class player extends entity{

    public player(int x, int y) {
        super(x, y, 2);
        image = new Image("");
    }

    public boolean push(entity obj) {
        if(obj.can_be_moved){
            obj.velocity_x = velocity_x;
            obj.velocity_y = velocity_y;
            return true; //成功移动
        }
        return false;
    }


    public void move(MapMatrix mapMatrix, entity[] entities) {
        boolean moving = true;
        for(entity e: entities) {
            if (e.x == x + velocity_x && e.y == y + velocity_y) {
                moving = moving && push(e);
            }
        }
        if(can_move(mapMatrix) && moving){
            mapMatrix.add(x,y, -(int)Math.pow(2, type));
            x += velocity_x;
            y += velocity_y;
            mapMatrix.add(x,y, (int)Math.pow(2, type)); //向那一格第i位加入
        }
    }

}
