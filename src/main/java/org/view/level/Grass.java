package org.view.level;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;
import org.model.config;

public class Grass {
    public Grass() {
    }
    public static int myRand(int a, int b, int c, int l, int r){
        int randomNumber = generatePseudoRandom(a, b, c);
//        System.out.println(a + " " + b + " " + c + " " + randomNumber);
        return (randomNumber % (r - l + 1)) + l;
    }
    public static Color randColor(int dx, int dy){
        // 没学号可用了，就用这个吧
        int R = (int)(config.themeColor.getRed() * 255) + myRand(dx, dy, 1, -10, 10);
        int G = (int)(config.themeColor.getGreen() * 255) + myRand(dx, dy, 2, -10, 10);
        int B = (int)(config.themeColor.getBlue() * 255) + myRand(dx, dy, 3, -10, 10);
        return Color.rgb(R, G, B);
    }
    public static int generatePseudoRandom(int x, int y, int z) {
        // 使用XORShift算法生成伪随机数
        x ^= x << 6;
        y ^= y << 5;
        z ^= z << 4;
        x ^= x >> 7;
        y ^= y >> 3;
        z ^= z >> 1;
        int result = (x ^ y ^ z);
        return result;
    }
    private int grasstimeid = 0;
    private static int[] grassMove = {0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, -1, -1, -1, -1, -1};
    public void addGrass(Canvas canvas, int dx, int dy, double anchor_posx, double anchor_posy, double size){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        int x = (int) (anchor_posx + dx * size);
        int y = (int) (anchor_posy + dy * size);

        gc.setFill(randColor(dx, dy));
        gc.fillRect(x, y, size, size);

        //给方形的边缘添加一些小方形，使其看起来更像草地
        int divide = 8;
        double dsize = size / divide;
        for (int i = 0; i < divide; ++i)
            if(myRand(dx, dy, i, -10, 10) < 0){
                gc.setFill(randColor(dx, dy - 1));
                gc.fillRect(x + (i + grassMove[( grasstimeid / 2 + myRand(dx, dy, 0, 0, 3)) % butterT]) * dsize, y, dsize, dsize);
            }
        //草片片
        for(int i = 1; i <= 3; ++i){
            if(myRand(dx, dy, i, 0, 1) == 0){
                int pieceX = (int) (anchor_posx + dx * size + myRand(dx, dy, i, 0, divide - 2) * dsize);
                int pieceY = (int) (anchor_posy + dy * size + myRand(dx, dy, -i, 0, divide - 1) * dsize);

                gc.setFill(randColor(dx, dy - 1));
                gc.fillRect(pieceX, pieceY, dsize, dsize * myRand(dx, dy, i, 2, 5));
            }
        }

       //花片片
        Color yellow = Color.rgb(240, 200, 100);
        Color white = Color.rgb(255, 255, 255);
        Color darkgreen = randColor(dx, dy);
        darkgreen = Color.rgb((int)(darkgreen.getRed() * 0.8 * 255), (int)(darkgreen.getGreen() * 0.8 * 255), (int)(darkgreen.getBlue() * 0.8 * 255));
        if(myRand(dx, dy, 0, 0, 5) == 0){
            int pieceX = (int) (anchor_posx + dx * size + myRand(dx, dy, 1, 1, divide - 2) * dsize);
            int pieceY = (int) (anchor_posy + dy * size + myRand(dx, dy, -1, 1, divide - 3) * dsize);
            //加深 green
            //绿色阴影
            gc.setFill(darkgreen);
            gc.fillRect(pieceX, pieceY + dsize * 2, dsize, dsize);
            gc.fillRect(pieceX - dsize, pieceY + dsize, dsize * 3, dsize);

            //白色花瓣
            gc.setFill(white);
            gc.fillRect(pieceX - dsize, pieceY, dsize * 3, dsize);
            gc.fillRect(pieceX, pieceY - dsize, dsize, dsize * 3);

            //黄色花蕊
            gc.setFill(yellow);
            gc.fillRect(pieceX, pieceY, dsize, dsize);
        }
    }
    private static int butterT = 16;
    private static int ButterDX[] = {0, 0, 1, 1, 2, 2, 3, 3, 3, 3, 2, 2, 1, 1, 0, 0};
    private static int ButterDY[] = {1, 1, 2, 2, 2, 2, 1, 1, 0, 0, -1, -1, -1, -1, 0, 0};
    private static int ButterHeight[] = {1, 0, 2, 2, 1, 2, 2, 2, 1, 0, 2, 2, 1, 2, 2, 2};
    private static int ButterWidth[] = {2, 2, 1, 2, 2, 0, 1, 2, 2, 2, 1, 2, 2, 0, 1, 2};
    private static int timeid = 0;
    public void updateTimeid(){
        grasstimeid = (grasstimeid + 1) % (butterT * 2);
        timeid = (timeid + 1) % butterT;
    }
    public void addButterfly(Pane root, int dx, int dy, double anchor_posx, double anchor_posy, double size) {
        int divide = 8;
        double dsize = size / divide;
        Color white = Color.rgb(255, 255, 255);
        int butterDX, butterDY;
        if(myRand(dx, dy, 1, 0, 1) == 0) butterDX = ButterDX[(timeid + myRand(dx, dy, 1, 0, butterT - 1)) % butterT];
        else butterDX = ButterDX[(-timeid + myRand(dx, dy, 1, 0, butterT - 1) + butterT) % ButterDX.length];
        if(myRand(dx, dy, 1, 0, 1) == 0) butterDY = ButterDY[(timeid + myRand(dx, dy, 1, 0, butterT - 1)) % butterT];
        else butterDY = ButterDY[(-timeid + myRand(dx, dy, 1, 0, butterT - 1) + butterT) % butterT];
        int butterHeight = ButterHeight[(timeid + myRand(dx, dy, 1, 0, butterT - 1)) % butterT];
        int butterWidth = ButterWidth[(timeid + myRand(dx, dy, 1, 0, butterT - 1)) % butterT];
        if (myRand(dx, dy, 0, 0, 30) == 0) {
            int pieceX = (int) ((anchor_posx + dx * size + myRand(dx, dy, 1, 1, divide - 1) * dsize) + butterDX * dsize);
            int pieceY = (int) ((anchor_posy + dy * size + myRand(dx, dy, -1, 1, divide - 1) * dsize) + butterDY * dsize);
            Rectangle butterfly = new Rectangle(pieceX, pieceY, dsize * butterWidth, dsize * butterHeight);
            butterfly.setFill(white);
            root.getChildren().add(butterfly);
        }
    }

    public static void addButterflyShadow(Pane root, int dx, int dy, double anchor_posx, double anchor_posy, double size) {
        int divide = 8;
        double dsize = size / divide;
        Color darkgreen = randColor(dx, dy);
        darkgreen = Color.rgb((int) (darkgreen.getRed() * 0.8 * 255), (int) (darkgreen.getGreen() * 0.8 * 255), (int) (darkgreen.getBlue() * 0.8 * 255));
        int butterDX, butterDY;
        if(myRand(dx, dy, 1, 0, 1) == 0) butterDX = ButterDX[(timeid + myRand(dx, dy, 1, 0, butterT - 1)) % butterT];
        else butterDX = ButterDX[(-timeid + myRand(dx, dy, 1, 0, butterT - 1) + butterT) % ButterDX.length];
        if(myRand(dx, dy, 1, 0, 1) == 0) butterDY = ButterDY[(timeid + myRand(dx, dy, 1, 0, butterT - 1)) % butterT];
        else butterDY = ButterDY[(-timeid + myRand(dx, dy, 1, 0, butterT - 1) + butterT) % butterT];
        if (myRand(dx, dy, 0, 0, 30) == 0) {
            int pieceX = (int) ((anchor_posx + dx * size + myRand(dx, dy, 1, 1, divide - 1) * dsize) + butterDX * dsize);
            int pieceY = (int) ((anchor_posy + dy * size + myRand(dx, dy, -1, 1, divide - 1) * dsize) + butterDY * dsize);
            Rectangle shade = new Rectangle(pieceX, pieceY + dsize * 4, dsize * 2, dsize * 2);
            shade.setFill(darkgreen);
            root.getChildren().add(shade);
        }
    }
}
