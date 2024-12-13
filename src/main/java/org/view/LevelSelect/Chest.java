package org.view.LevelSelect;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.model.config;


public class Chest {
    Image chest_open = new Image(getClass().getResourceAsStream("/images/treasure_opened.png"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image chest_closed = new Image(getClass().getResourceAsStream("/images/treasure_closed.png"), config.Map_Node_Width, config.Map_Node_Width, false, false);

    ImageView imageView = new ImageView(chest_closed);

    int x, y;

    public Chest(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void open() {
        imageView.setImage(chest_open);
    }
}
