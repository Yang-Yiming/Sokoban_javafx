package model;

import java.util.ArrayList;

public class MapMatrix {
    private int[][] matrix; // 二进制下， 第一位表示是否有墙，第二位是否有箱子，第三位是否有玩家，第四位是否有goal


    public boolean isOne(int num, int n){
        return ((num>>(n-1)&1)==1);
    }

    public boolean hasNothing(int x, int y){
        return matrix[x][y] == 0;
    }

    public boolean hasWall(int x, int y){
        return isOne(matrix[x][y], 0);
    }
    public boolean hasBox(int x, int y){
        return isOne(matrix[x][y], 1);
    }
    public boolean hasPlayer(int x, int y){
        return isOne(matrix[x][y], 2);
    }
    public boolean hasGoal(int x, int y){
        return isOne(matrix[x][y], 3);
    }

    public void set(int x, int y, int num){
        matrix[x][y] = num;
    }
    public void add(int x, int y, int num){
        matrix[x][y] += num;
    }

    public int getWidth() {
        return this.matrix[0].length;
    }

    public int getHeight() {
        return this.matrix.length;
    }


}
