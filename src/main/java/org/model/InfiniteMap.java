package org.model;

import java.util.HashMap;

public class InfiniteMap extends GameMap {

    HashMap<Coordinate, Integer> matrix = new HashMap<>(); // 二进制下， 第0位表示是否有墙，第1位是否有箱子，第2位是否有玩家，第3位是否有goal
    HashMap<Coordinate, Integer> box_matrix = new HashMap<>();

    private int left_boundary=0, right_boundary=0, up_boundary=0, down_boundary=0;
    private int sublevel_begin_x, sublevel_begin_y;
    private int win_check_width, win_check_height;

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
                if(data[i][j] != DEFAULT_VALUE && ((data[i][j] >> 2) & 1) != 1) { // 防止出现好几个player
                    set(begin_x + j, begin_y + i, data[i][j]);
                }else if(data[i][j] != DEFAULT_VALUE){
                    set(begin_x + j, begin_y + i, data[i][j] - (1 << 2));
                }
            }
        }
    }

    public void add_level(int begin_x, int begin_y, int[][] data) {
        add_data(begin_x, begin_y, data);
        sublevel_begin_x = begin_x; sublevel_begin_y = begin_y;
        win_check_width = data[0].length; win_check_height = data.length;
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
        set(x,y,get(x,y) - (1 << type));
    }

    public void setBox_matrix(int x, int y, int num) {
        if(num == 0){
            box_matrix.remove(new Coordinate(x, y));
            return;
        }
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

    public HashMap<Coordinate, Integer> getMatrixMap() {
        return matrix;
    }
    public HashMap<Coordinate, Integer> getBox_matrix() { return box_matrix; }

    public int getWidth() {
        int width = right_boundary - left_boundary + 1;
        return width;
        //return (int) Math.min(width, (double) config.ScreenWidth / config.tile_size * 1.2);
    }

    public int getHeight() {
        int height = down_boundary - up_boundary + 1;
        return height;
        //return (int) Math.min(height, (double) config.ScreenHeight / config.tile_size * 1.2);
    }

    public int getRight_boundary() {
        return right_boundary;
    }
    public int getDown_boundary() {
        return down_boundary;
    }
    public int getLeft_boundary() {
        return left_boundary;
    }
    public int getUp_boundary() {
        return up_boundary;
    }
    public int getSublevel_begin_x() {
        return sublevel_begin_x;
    }
    public int getSublevel_begin_y() {
        return sublevel_begin_y;
    }
    public int getWin_check_width() {
        return win_check_width;
    }
    public int getWin_check_height() {
        return win_check_height;
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

    public void add_line(int x1, int y1, int dx, int dy, int value){
        for(int x = x1; x < x1 + dx; x++){
            set(x, y1, value);
        }
        for(int y = y1; y < y1 + dy; y++){
            set(x1, y, value);
        }
    }

    public void add_line(int x1, int y1, boolean dir, int value, int stop, boolean set_last_0, int max_len) {
        // dir : true -> x, false -> y
        if(dir){
            while(get(x1, y1) != stop && max_len > 0){
                set(x1, y1, value);
                x1++; max_len--;
            }
        } else {
            while(get(x1, y1) != stop && max_len > 0){
                set(x1, y1, value);
                y1++; max_len--;
            }
        }
        if(set_last_0){
            set(x1, y1, 0);
        }
    }

    public void add_road(int x1, int y1, int width, int length, Direction direction) {
    switch (direction) {
        case Direction.RIGHT:
//            add_line(x1, y1, true, 1, 1, false, length + 5);
//            add_line(x1, y1 + width - 1, true, 1, 1, false, length + 5);
            add_line(x1, y1, length, 0, 1);
            add_line(x1, y1 + width - 1, length, 0, 1);
            for (int i = 1; i < width - 1; i++) {
//                add_line(x1, y1 + i, true, 0, 1, true, length + 5);
                add_line(x1, y1 + i, length, 0, 0);
            }
            break;
        case Direction.DOWN:
            add_line(x1, y1, 0, length, 1);
            add_line(x1 + width - 1, y1, 0, length, 1);
            for (int i = 1; i < width - 1; i++) {
                add_line(x1 + i, y1, 0, length, 0);
            }
            break;
        case Direction.UP:
            add_line(x1, y1 - length, 0, length, 1);
            add_line(x1 + width - 1, y1 - length, 0, length, 1);
            for (int i = 1; i < width - 1; i++) {
                add_line(x1 + i, y1 - length, 0, length, 0);
            }
            break;
    }
}

}
