package model;

public class MapMatrix {
    private int[][] matrix; // 二进制下， 第一位表示是否有墙，第二位是否有箱子，第三位是否有玩家，第四位是否有goal


    public boolean isOne(int num, int n){
        return ((num>>(n-1)&1)==1);
    }

    public boolean hasNothing(int x, int y){
        return matrix[x][y] == 0;
    }
    public boolean hasNoObstacle(int x, int y){
        int[] ObstacleTypes = {1, 2, 3}; // wall box player 会阻挡
        for(int e: ObstacleTypes){
            if(isOne(matrix[x][y], e)){
                return false;
            }
        }
        return true;
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

    public MapMatrix(int[][] matrix) {
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[i].length; j++){
                set(i,j,matrix[i][j]);
            }
        }
    }


}
