package org.model;

public class MapMatrix extends GameMap {
    private int[][] matrix; // 二进制下， 第0位表示是否有墙，第1位是否有箱子，第2位是否有玩家，第3位是否有goal
    private int[][] box_matrix;

    public boolean hasNothing(int x, int y) {
        return matrix[y][x] == 0;
    }

    public boolean hasNoObstacle(int x, int y) {
        if(x < 0 || x >= this.getWidth() || y < 0 || y >= this.getHeight()) {
            return true;
        }
        int[] ObstacleTypes = { 0, 1, 2 }; // wall box player 会阻挡
        for (int e : ObstacleTypes) {
            if (isOne(this.matrix[y][x], e)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasWall(int x, int y) {
        return isOne(matrix[y][x], 0);
    }

    public boolean hasBox(int x, int y) {
        return isOne(matrix[y][x], 1);
    }

    public boolean hasPlayer(int x, int y) {
        return isOne(matrix[y][x], 2);
    }

    public boolean hasGoal(int x, int y) {
        return isOne(matrix[y][x], 3);
    }

    public void set(int x, int y, int num) {
        matrix[y][x] = num;
    }

    public void add(int x, int y, int type) {
        matrix[y][x] += (1 << type);
    }

    public void remove(int x, int y, int type) {
        matrix[y][x] -= (1 << type);
    }

    public void setBox_matrix(int x, int y, int num) {
        box_matrix[y][x] = num;
    }
    public int getBox_matrix_id(int x, int y) {
        return box_matrix[y][x];
    }

    public int getWidth() {
        return this.matrix[0].length;
    }

    public int getHeight() {
        return this.matrix.length;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public MapMatrix(int[][] matrix) {
        super();
        this.matrix = new int[matrix.length][matrix[0].length];
        this.box_matrix = new int[matrix.length][matrix[0].length];
        int box_index = 1; // 编号从1开始

        for (int y = 0; y < matrix.length; ++y) {
            for (int x = 0; x < matrix[y].length; ++x) {
                set(x, y, matrix[y][x]);
                if(hasBox(x, y)) {
                    box_matrix[y][x] = box_index++;
                } else {
                    box_matrix[y][x] = 0;
                }
            }
        }
    }

}
