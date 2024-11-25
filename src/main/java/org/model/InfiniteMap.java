package org.model;

import java.util.HashMap;

public class InfiniteMap {

    public static class vector { // 二维坐标
        public int x, y;
        public vector(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    HashMap<vector, Integer> map = new HashMap<>();
    private int DEFAULT_VALUE;

    public InfiniteMap() {
        map = new HashMap<>();
        DEFAULT_VALUE = 0;
    }
    public InfiniteMap(int DEFAULT_VALUE) {
        map = new HashMap<>();
        this.DEFAULT_VALUE = DEFAULT_VALUE;
    }

    public void set(int x, int y, int value) {
        map.put(new vector(x, y), value);
    }
    public void get(int x, int y) {
        map.getOrDefault(new vector(x, y), DEFAULT_VALUE);
    }

    public void set_data(int begin_x, int begin_y, int[][] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                set(begin_x + i, begin_y + j, data[i][j]);
            }
        }
    }

    public void add_data(int begin_x, int begin_y, int[][] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(data[i][j] != DEFAULT_VALUE) {
                    set(begin_x + i, begin_y + j, data[i][j]);
                }
            }
        }
    }

    public void delete(int x, int y) {
        map.remove(new vector(x, y));
    }

    public void clear(int begin_x, int begin_y, int end_x, int end_y) {
        for (int i = begin_x; i <= end_x; i++) {
            for (int j = begin_y; j <= end_y; j++) {
                delete(i, j);
            }
        }
    }

    public void clearAll() {
        map.clear();
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

}
