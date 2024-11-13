package org.model;

public class MapMatrix {
    private int[][] matrix; // 二进制下， 第0位表示是否有墙，第1位是否有箱子，第2位是否有玩家，第3位是否有goal
    public boolean isOne(int num, int n) {
        return (((num >> n) & 1) == 1);
    }

    public boolean hasNothing(int x, int y) {
        return matrix[y][x] == 0;
    }

    public boolean hasNoObstacle(int x, int y) {
        int[] ObstacleTypes = { 0, 1, 2 }; // wall box player 会阻挡
        for (int e : ObstacleTypes) {
            if (isOne(this.matrix[y][x], e)) {
                // System.out.println(x + " " + y + " " + matrix[y][x] + " "  + e);
                // for(int i = 0; i < this.getHeight(); ++i){
                //     for(int j = 0; j < this.getWidth(); ++j){
                //         System.out.print(this.matrix[i][j] + " ");
                //     }
                //     System.out.println();
                // }
        
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

    public int getWidth() {
        return this.matrix[0].length;
    }

    public int getHeight() {
        return this.matrix.length;
    }

    public MapMatrix(int[][] matrix) {
        this.matrix = new int[matrix.length][matrix[0].length];
        for (int y = 0; y < matrix.length; ++y) {
            for (int x = 0; x < matrix[y].length; ++x) {
                set(x, y, matrix[y][x]);
            }
        }
    }

}
