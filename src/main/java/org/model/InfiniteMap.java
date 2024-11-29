//package org.model;
//
//import java.util.HashMap;
//
//public class InfiniteMap extends GameMap {
//
//    public static class vector { // 二维坐标
//        public int x, y;
//        public vector(int x, int y) {
//            this.x = x;
//            this.y = y;
//        }
//    }
//
//    HashMap<vector, Integer> matrix = new HashMap<>();
//    HashMap<vector, Integer> box_matrix = new HashMap<>();
//    private int DEFAULT_VALUE;
//
//    public InfiniteMap() {
//        super();
//        matrix = new HashMap<>();
//        box_matrix = new HashMap<>();
//        DEFAULT_VALUE = 0;
//    }
//    public InfiniteMap(int DEFAULT_VALUE) {
//        super();
//        matrix = new HashMap<>();
//        box_matrix = new HashMap<>();
//        this.DEFAULT_VALUE = DEFAULT_VALUE;
//    }
//
//    public void set(int x, int y, int value) {
//        matrix.put(new vector(x, y), value);
//    }
//    public int get(int x, int y) {
//        return matrix.getOrDefault(new vector(x, y), DEFAULT_VALUE);
//    }
//
//    public void set_data(int begin_x, int begin_y, int[][] data) {
//        for (int i = 0; i < data.length; i++) {
//            for (int j = 0; j < data[i].length; j++) {
//                set(begin_x + i, begin_y + j, data[i][j]);
//            }
//        }
//    }
//
//    public void add_data(int begin_x, int begin_y, int[][] data) {
//        for (int i = 0; i < data.length; i++) {
//            for (int j = 0; j < data[i].length; j++) {
//                if(data[i][j] != DEFAULT_VALUE) {
//                    set(begin_x + i, begin_y + j, data[i][j]);
//                }
//            }
//        }
//    }
//
//    public void delete(int x, int y) {
//        matrix.remove(new vector(x, y));
//    }
//
//    public void clear(int begin_x, int begin_y, int end_x, int end_y) {
//        for (int i = begin_x; i <= end_x; i++) {
//            for (int j = begin_y; j <= end_y; j++) {
//                delete(i, j);
//            }
//        }
//    }
//
//    public void clearAll() {
//        matrix.clear();
//    }
//
//    // 加入一条道路
//    public void add_road(int begin_x, int begin_y, int width, int length, Direction direction) {
//        if(direction == Direction.LEFT) {
//            for(int i = begin_x; i > begin_x - length; i--) {
//                set(i, begin_y, 1); set(i, begin_y + width - 1, 1);
//            }
//        } else if (direction == Direction.RIGHT) {
//            for(int i = begin_x; i < begin_x + length; i++) {
//                set(i, begin_y, 1); set(i, begin_y + width - 1, 1);
//            }
//        } else if (direction == Direction.UP) {
//            for(int i = begin_y; i > begin_y - length; i--) {
//                set(begin_x, i, 1); set(begin_x + width - 1, i, 1);
//            }
//        } else if (direction == Direction.DOWN) {
//            for(int i = begin_y; i < begin_y + length; i++) {
//                set(begin_x, i, 1); set(begin_x + width - 1, i, 1);
//            }
//        }
//    }
//
//
//    // 与正常map一样的部分
//    public boolean isOne(int num, int n) {
//        return (((num >> n) & 1) == 1);
//    }
//
//    public boolean hasNothing(int x, int y) {
//        return this.get(x, y) == 0;
//    }
//
//    public boolean hasNoObstacle(int x, int y) {
//        int[] ObstacleTypes = { 0, 1, 2 }; // wall box player 会阻挡
//        for (int e : ObstacleTypes) {
//            if (isOne(this.get(x,y), e)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public boolean hasWall(int x, int y) {
//        return isOne(this.get(x,y), 0);
//    }
//
//    public boolean hasBox(int x, int y) {
//        return isOne(get(x,y), 1);
//    }
//
//    public boolean hasPlayer(int x, int y) {
//        return isOne(get(x,y), 2);
//    }
//
//    public boolean hasGoal(int x, int y) {
//        return isOne(get(x,y), 3);
//    }
//
//    public void add(int x, int y, int type) {
//        set(x,y,get(x,y) + (1 << type));
//    }
//
//    public void remove(int x, int y, int type) {
//        set(x,y,get(x,y)- (1 << type));
//    }
//
//    public void setBox_matrix(int x, int y, int num) {
//        box_matrix.put(new vector(x, y), num);
//    }
//    public int getBox_matrix_id(int x, int y) {
//        return box_matrix.getOrDefault(new vector(x, y), 0);
//    }
//
//    public HashMap<vector, Integer> getMatrix() {
//        return matrix;
//    }
//
//}
