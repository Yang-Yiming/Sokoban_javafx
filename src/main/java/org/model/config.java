package org.model;

import java.lang.Math;

public class config {
    //Constants
    public static final int tile_size = 55;
    public static final int ScreenHeight = 600;
    public static final int ScreenWidth = 800;
    public static final int Map_Node_Width = 60;
    public static final int Map_Node_Height = 20;
    public static final int Map_Layer_Gap = 40;
    public static final int Map_Node_Gap = 20;
    public static final int Win_Rect_Height = 0;
    public static final int Win_Rect_Stroke = 8;
    public static final double win_in_duration = 0.15;
    public static final double win_pause_duration = 0.5;
    public static final double font_size_1 = 30;
    public static final double font_size_2 = 35;
    public static final int font_size_change_millis = 100;

    // 可以用设置改变的常量
    public static boolean is_vertical = false;
    public static int move_anim_duration = 250; // milliseconds
    public static int fade_anim_duration = 250;
    public static int save_text_maintain = 1000;
    public static double wall_angle_amount = 0.5;
    public static double box_angle_amount = 0.3;
    public static int button_size = 50;
    public static int button_gap = 8;
    public static int mode = 0;

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

    public static double is_linear(int x1, int y1, int x2, int y2, int x, int y) {
        return Math.abs((x - x1) * (y2 - y1) - (y - y1) * (x2 - x1));
    }
    public static final double EPS = 10.0;

}
