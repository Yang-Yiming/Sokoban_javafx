package org.view.game;

import javafx.scene.image.Image;
import org.model.MapMatrix;
import org.view.map.map;

import java.util.ArrayList;
import java.util.Map;

public class player extends entity {

    public player(int x, int y) {
        super(x, y, 2);
        // image = new Image("");
    }

    public boolean push(entity obj, MapMatrix map) {
        if (obj.can_move(map, this.velocity_x, this.velocity_y)) {
            obj.velocity_x = this.velocity_x;
            obj.velocity_y = this.velocity_y;
            return true; // 成功移动
        }
        return false;
    }

    public void move(MapMatrix map, ArrayList<box> entities, int[][] boxMatrix) {
        int newx = x + velocity_x;
        int newy = y + velocity_y;
        if(can_move(map, velocity_x, velocity_y)){
            this.move(map);
        }else if(map.hasBox(newx, newy)){ // 暂时先这么写
            box e = entities.get(boxMatrix[newy][newx] - 1);
            if(push(e, map)){
                boxMatrix[e.get_y()][e.get_x()] = 0;
                e.move(map);
                boxMatrix[e.get_y()][e.get_x()] = e.id;
                // for(int i = 0; i < map.getHeight(); ++i){
                //     for(int j = 0; j < map.getWidth(); ++j){
                //         System.out.print(boxMatrix[i][j] + " ");
                //     }
                //     System.out.println();
                // }
                this.move(map);
            }
        }

    }

    public void set_velocity(int x, int y) {
        velocity_x = x;
        velocity_y = y;
    }

}
