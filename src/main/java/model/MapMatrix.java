package model;

import java.util.ArrayList;

public class MapMatrix {
    private ArrayList<Integer>[][] matrix;
    private ArrayList<view.game.entity>[][] obj_matrix; // 感觉这个方法空间复杂度有点高了 但我没想到其他的

    public MapMatrix(ArrayList<Integer>[][] matrix) {
        this.matrix = matrix;
    }
    public MapMatrix(int[][] matrix) {
        //以后再写 懒了（
    }

    public int getWidth() {
        return this.matrix[0].length;
    }

    public int getHeight() {
        return this.matrix.length;
    }

    public ArrayList<Integer> get_objects_id(int row, int col) {
        return matrix[row][col];
    }

    public ArrayList<view.game.entity> get_objects(int row, int col) { return obj_matrix[row][col]; }

    public ArrayList<Integer>[][] getMatrix() {
        return matrix;
    }
}
