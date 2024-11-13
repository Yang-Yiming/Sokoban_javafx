package org.view.game;

import javafx.scene.image.Image;

public class box extends entity {
    public int id;
    public box(int x, int y, int id) {
        super(x, y, 1);
        this.id = id;
        //image = new Image("");
    }

}
