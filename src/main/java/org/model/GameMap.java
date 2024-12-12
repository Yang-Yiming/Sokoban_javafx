package org.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class GameMap{
    public boolean isOne(int num, int n) {
        return (((num >> n) & 1) == 1);
    }

    public abstract int get(int x, int y);
    public abstract void set(int x, int y, int num);

    public boolean hasNothing(int x, int y) {
        return this.get(x, y) == 0;
    }

    public boolean hasNoObstacle(int x, int y) {
        if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) return true;
        int[] ObstacleTypes = { 0, 1, 2 }; // wall box player 会阻挡
        for (int e : ObstacleTypes) {
            if (isOne(get(x,y), e)) {
                return false;
            }
        }
        return true;
    }
    public Image getWallImage(int x, int y){
        if(hasWall(x, y - 1)){
            if(hasWall(x - 1, y) && hasWall(x + 1, y)) return new Image(getClass().getResourceAsStream("/images/wall/wall_md.png"), config.tile_size, config.tile_size, false, false);
            else if(hasWall(x - 1, y)) return new Image(getClass().getResourceAsStream("/images/wall/wall_rd.png"), config.tile_size, config.tile_size, false, false);
            else if(hasWall(x + 1, y)) return new Image(getClass().getResourceAsStream("/images/wall/wall_ld.png"), config.tile_size, config.tile_size, false, false);
            else return new Image(getClass().getResourceAsStream("/images/wall/wall_d.png"), config.tile_size, config.tile_size, false, false);
        }else{
            if(hasWall(x - 1, y) && hasWall(x + 1, y)) return new Image(getClass().getResourceAsStream("/images/wall/wall_m.png"), config.tile_size, config.tile_size, false, false);
            else if(hasWall(x - 1, y)) return new Image(getClass().getResourceAsStream("/images/wall/wall_r.png"), config.tile_size, config.tile_size, false, false);
            else if(hasWall(x + 1, y)) return new Image(getClass().getResourceAsStream("/images/wall/wall_l.png"), config.tile_size, config.tile_size, false, false);
            else return new Image(getClass().getResourceAsStream("/images/wall/wall.png"), config.tile_size, config.tile_size, false, false);
        }
    }
    public boolean hasWall(int x, int y) {
        if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) return false;
        return isOne(get(x,y), 0);
    }

    public boolean hasBox(int x, int y) {
        return isOne(get(x,y), 1);
    }

    public boolean hasPlayer(int x, int y) {
        return isOne(get(x,y), 2);
    }

    public boolean hasGoal(int x, int y) {
        return isOne(get(x,y), 3);
    }

    public void add(int x, int y, int type) {
        set(x,y,get(x,y) + (1 << type));
    }

    public void remove(int x, int y, int type) {
        set(x,y,get(x,y)- (1 << type));
    }

    public abstract void setBox_matrix(int x, int y, int num);
    public abstract int getBox_matrix_id(int x, int y);

    public abstract int getWidth();
    public abstract int getHeight();

    public abstract int[][] getMatrix();
}