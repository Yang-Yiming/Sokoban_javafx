package model;

public class MapMatrix {

    enum GridType{
        EMPTY, WALL, GOAL;
    }

    private int[][] matrix;

    public MapMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getWidth() {
        return this.matrix[0].length;
    }

    public int getHeight() {
        return this.matrix.length;
    }

    public int getId(int row, int col) {
        return matrix[row][col];
    }

    public int[][] getMatrix() {
        return matrix;
    }
}
