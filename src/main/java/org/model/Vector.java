package org.model;

public class Vector { // 二维坐标
    public int x, y;
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass()!= obj.getClass())
            return false;
        Vector other = (Vector) obj;
        if (x!= other.x)
            return false;
        if (y!= other.y)
            return false;
        return true;
    }
}
