package org.model;

import java.util.HashMap;

public abstract class GameMap {
    public boolean isOne(int num, int n) {
        return (((num >> n) & 1) == 1);
    }

    public abstract boolean hasNothing(int x, int y);

    public abstract boolean hasNoObstacle(int x, int y);

    public abstract boolean hasWall(int x, int y);

    public abstract boolean hasBox(int x, int y);

    public abstract boolean hasPlayer(int x, int y);

    public abstract boolean hasGoal(int x, int y);

    public abstract void add(int x, int y, int type);

    public abstract void remove(int x, int y, int type);

    public abstract void setBox_matrix(int x, int y, int num);
    public abstract int getBox_matrix_id(int x, int y);

    public abstract int getWidth();
    public abstract int getHeight();
}
