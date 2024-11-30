package org.model;

import java.util.HashMap;

public class InfiniteMap extends GameMap {

    HashMap<Coordinate, Integer> matrix = new HashMap<>(); // 二进制下， 第0位表示是否有墙，第1位是否有箱子，第2位是否有玩家，第3位是否有goal
    HashMap<Coordinate, Integer> box_matrix = new HashMap<>();

    private int left_boundary=0, right_boundary=0, up_boundary=0, down_boundary=0;

    private int DEFAULT_VALUE;

    public void set(int x, int y, int value) {
        if(value == 0){
            matrix.remove(new Coordinate(x, y));
            return;
        }
        matrix.put(new Coordinate(x, y), value);
        if(x < left_boundary) left_boundary = x;
        if(x > right_boundary) right_boundary = x;
        if(y < up_boundary) up_boundary = y;
        if(y > down_boundary) down_boundary = y;
    }
    public int get(int x, int y) {
        return matrix.getOrDefault(new Coordinate(x, y), DEFAULT_VALUE);
    }

    public void set_data(int begin_x, int begin_y, int[][] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                set(begin_x + j, begin_y + i, data[i][j]);
            }
        }
    }

    public void add_data(int begin_x, int begin_y, int[][] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(data[i][j] != DEFAULT_VALUE) {
                    set(begin_x + j, begin_y + i, data[i][j]);
                }
            }
        }
    }

    public void delete(int x, int y) {
        matrix.remove(new Coordinate(x, y));
    }

    public void clear(int begin_x, int begin_y, int end_x, int end_y) {
        for (int i = begin_x; i <= end_x; i++) {
            for (int j = begin_y; j <= end_y; j++) {
                delete(i, j);
            }
        }
    }

    public void clearAll() {
        matrix.clear();
    }

    // 加入一条道路
    public void add_road(int begin_x, int begin_y, int width, int length, Direction direction) {
        if(direction == Direction.LEFT) {
            for(int i = begin_x; i > begin_x - length; i--) {
                set(i, begin_y, 1); set(i, begin_y + width - 1, 1);
            }
        } else if (direction == Direction.RIGHT) {
            for(int i = begin_x; i < begin_x + length; i++) {
                set(i, begin_y, 1); set(i, begin_y + width - 1, 1);
            }
        } else if (direction == Direction.UP) {
            for(int i = begin_y; i > begin_y - length; i--) {
                set(begin_x, i, 1); set(begin_x + width - 1, i, 1);
            }
        } else if (direction == Direction.DOWN) {
            for(int i = begin_y; i < begin_y + length; i++) {
                set(begin_x, i, 1); set(begin_x + width - 1, i, 1);
            }
        }
    }

    // 与正常map一样的部分
//    // public boolean isOne(int num, int n) {
//        return (((num >> n) & 1) == 1);
//    }
  
    public boolean hasNothing(int x, int y) {
        return this.get(x, y) == 0;
    }

    public boolean hasNoObstacle(int x, int y) {
        int[] ObstacleTypes = { 0, 1, 2 }; // wall box player 会阻挡
        for (int e : ObstacleTypes) {
            if (isOne(get(x,y), e)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasWall(int x, int y) {
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

    public void setBox_matrix(int x, int y, int num) {
        box_matrix.put(new Coordinate(x, y), num);
    }
    public int getBox_matrix_id(int x, int y) {
        return box_matrix.getOrDefault(new Coordinate(x, y), 0);
    }

    public int[][] getMatrix() {
        int[][] ans = new int[getHeight()][getWidth()];
        for (int y = up_boundary; y < down_boundary; ++y) {
            for (int x = left_boundary; x < right_boundary; ++x) {
                ans[y - up_boundary][x - left_boundary] = get(x, y);
            }
        }
        return ans;
    }

    public int getWidth() {
        int width = right_boundary - left_boundary + 1;
        return (int) Math.min(width, (double) config.ScreenWidth / config.tile_size * 1.2);
    }

    public int getHeight() {
        int height = down_boundary - up_boundary + 1;
        return (int) Math.min(height, (double) config.ScreenHeight / config.tile_size * 1.2);
    }

    public void add_line(int x1, int y1, int x2, int y2, int type) {
        for(int x = x1; x <= x2; x++) {
            for(int y = y1; y<=y2; y++) {
                set(x,y,type);
                if(config.is_linear(x1,y1,x2,y2,x,y) < config.EPS) {
                    break;
                }
            }
        }
    }

    public InfiniteMap(int[][] matrix) {
        set_data(0,0,matrix);

        box_matrix = new HashMap<>();
        int box_index = 1; // 编号从1开始

        for (int y = 0; y < matrix.length; ++y) {
            for (int x = 0; x < matrix[y].length; ++x) {
                set(x, y, matrix[y][x]);
                if(hasBox(x, y)) {
                    setBox_matrix(x, y, box_index++);
                } else {
                    setBox_matrix(x, y, 0);
                }
            }
        }
    }
    public InfiniteMap() {
        matrix = new HashMap<>();
        box_matrix = new HashMap<>();
        DEFAULT_VALUE = 0;
    }
    public InfiniteMap(int DEFAULT_VALUE) {
        matrix = new HashMap<>();
        box_matrix = new HashMap<>();
        this.DEFAULT_VALUE = DEFAULT_VALUE;
    }

}
