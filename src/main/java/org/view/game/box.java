package org.view.game;

import javafx.scene.image.Image;
import org.model.MapMatrix;

public class box extends entity {
    public int id;
    public box(int x, int y, int id) {
        super(x, y, 1);
        this.id = id;
        //image = new Image(getClass().getResourceAsStream("images/box.png")); //找不到这个文件，不知道为什么
    }

    @Override
    public void move(MapMatrix map) {
        if (can_move(map, velocity_x, velocity_y)) {
            map.remove(x, y, type);
            map.setBox_matrix(x, y,0); // 把原来位置的箱子干掉
            x += velocity_x;
            y += velocity_y;
            map.add(x, y, type); // 向那一格第i位加入
            map.setBox_matrix(x, y, id); // 把新的箱子放进去
        }
    }

}
