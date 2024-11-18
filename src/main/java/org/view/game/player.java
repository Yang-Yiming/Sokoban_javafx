package org.view.game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.model.MapMatrix;
import org.model.config;

import java.util.ArrayList;

public class player extends entity {
//    private Image image;
//    private ImageView imageView;

    public player(int x, int y) {
        super(x, y, 2);
        image = new Image(getClass().getResourceAsStream("/images/player.png"));
        imageView = new ImageView(image);
        imageView.setFitHeight(config.tile_size);
        imageView.setFitWidth(config.tile_size);
    }

    public boolean push(entity obj, MapMatrix map) {
        if (obj.can_move(map, this.velocity_x, this.velocity_y)) {
            obj.velocity_x = this.velocity_x;
            obj.velocity_y = this.velocity_y;
            return true; // 成功移动
        }
        return false;
    }

    public void move(MapMatrix map, ArrayList<box> entities) {
        int newx = x + velocity_x;
        int newy = y + velocity_y;
        if(can_move(map, velocity_x, velocity_y)){
            this.move(map);
        } else if(map.hasBox(newx, newy)) { // 暂时先这么写
            box e = entities.get(map.getBox_matrix_id(newx, newy) - 1); // 获得那个被推的箱子
            if(push(e, map)){
                //map.setBox_matrix(e.get_x(), e.get_y(), 0);
                e.move(map);
                //map.setBox_matrix(e.get_x(), e.get_y(), e.id);
                this.move(map);
//                e.setMoving(true);
            }
        }
    }

    public void set_velocity(int x, int y) {
        velocity_x = x;
        velocity_y = y;
    }

    public Image getImage() {
        return image;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
