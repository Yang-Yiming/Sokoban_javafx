package org.view.game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.model.MapMatrix;
import org.model.config;

public class box extends entity {
    public int id;
    private Image image;
    private ImageView imageView;
    private boolean isMoving = false;
    public box(int x, int y, int id) {
        super(x, y, 1);
        this.id = id;
        image = new Image(getClass().getResourceAsStream("/images/box.png"));
        imageView = new ImageView(image);
        imageView.setFitHeight(config.tile_size);
        imageView.setFitWidth(config.tile_size);
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

    public Image getImage() {
        return image;
    }
    public ImageView getImageView() {
        return imageView;
    }

    public boolean isMoving() {
        return isMoving;
    }
    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

}
