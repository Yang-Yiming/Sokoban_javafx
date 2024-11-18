package org.view.game;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.model.MapMatrix;
import org.model.config;

public class box extends entity {
    public int id;
//    private Image image;
//    private ImageView imageView;
//    private boolean isMoving = false;
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
            // 动画
            TranslateTransition transition = new TranslateTransition(Duration.millis(config.move_anim_duration * 1000), imageView);
            transition.setFromX(0);
            transition.setFromY(0); // 我不理解 但能跑就行 所以说用的是相对坐标？（

            map.remove(x, y, type);
            map.setBox_matrix(x, y,0); // 把原来位置的箱子干掉
            x += velocity_x;
            y += velocity_y;
            map.add(x, y, type); // 向那一格第i位加入
            map.setBox_matrix(x, y, id); // 把新的箱子放进去

            transition.setToX(velocity_x * config.tile_size);
            transition.setToY(velocity_y * config.tile_size);
            transition.play();
        }
    }

    public Image getImage() {
        return image;
    }
    public ImageView getImageView() {
        return imageView;
    }

//    public boolean isMoving() {
//        return isMoving;
//    }
//    public void setMoving(boolean isMoving) {
//        this.isMoving = isMoving;
//    }

}
