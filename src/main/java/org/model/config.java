package org.model;

import java.lang.Math;

public class config {
    //Constants
    public static final double move_anim_duration = 0.1;
    public static final int tile_size = 50;
    public static final int ScreenWidth = 800;
    public static final int ScreenHeight = 600;

    public static final int Map_Node_Width = 50;
    public static final int Map_Node_Height = 20;
    public static final int Map_Space_Width = 60;
    public static final int Map_Space_Height = 200;

    //Methods
    public static double EaseInOutCubic(double begin, double end, double t) { // t是占总时间的比例
        double change = end - begin;
        t = 1.0 / t; // 动画的总时间默认为1秒，这里将t归一化
        if (t < 0.5) {
            // 在前半段动画中进行缓和加速
            return change / 2 * Math.pow(t * 2, 3) + begin;
        } else {
            // 在后半段动画中进行缓和减速
            return change / 2 * ((Math.pow(t * 2 - 2, 3) + 2)) + begin;
        }
    }

    public static int randint(int a, int b){
        return (int)(Math.random() * (b - a)) + a;
    }

}
