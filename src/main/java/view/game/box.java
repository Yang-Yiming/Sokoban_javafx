package view.game;

import model.MapMatrix;

public class box {
    private int x;
    private int y;

    private int add_x;
    private int add_y;

    box (int x, int y) {
        this.x = x; this.y = y;
    }

    public boolean can_move(MapMatrix matrix) {
        return(matrix.getId(x + add_x, y + add_y) == 0);
    }

    public void move(MapMatrix matrix) {
        if(can_move(matrix)) {
            x += add_x; y += add_y;
        }
    }

}
