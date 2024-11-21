package org.view.level;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.model.config;

public class Grass {
    public static int myRand(int a, int b, int c, int l, int r){
        int randomNumber = generatePseudoRandom(a, b, c);
//        System.out.println(a + " " + b + " " + c + " " + randomNumber);
        return (randomNumber % (r - l + 1)) + l;
    }
    public static Color randColor(int dx, int dy){
        // 没学号可用了，就用这个吧
        int R = 124 + myRand(dx, dy, 1, -10, 10);
        int G = 113 + myRand(dx, dy, 2, -10, 10);
        int B = 32 + myRand(dx, dy, 3, -10, 10);
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
    public static void addGrass(Pane root, int dx, int dy, double anchor_posx, double anchor_posy, double size){
        int x = (int) (anchor_posx + dx * size);
        int y = (int) (anchor_posy + dy * size);
        Rectangle grass = new Rectangle(x, y, size, size);
        grass.setFill(randColor(dx, dy));
        root.getChildren().add(grass);
        //给方形的边缘添加一些小方形，使其看起来更像草地
        int divide = 8;
        double dsize = size / divide;
        for (int i = 0; i < divide; ++i)
            if(myRand(dx, dy, i, 0, 1) == 0){
                Rectangle smallGrass = new Rectangle(x + i * dsize, y, dsize, dsize);
                smallGrass.setFill(randColor(dx, dy - 1));
                root.getChildren().add(smallGrass);
            }
        //草片片
        for(int i = 1; i <= 3; ++i){
            if(myRand(dx, dy, i, 0, 1) == 0){
                int pieceX = (int) (anchor_posx + dx * size + myRand(dx, dy, i, 0, divide - 1) * dsize);
                int pieceY = (int) (anchor_posy + dy * size + myRand(dx, dy, -i, 0, divide - 1) * dsize);
                Rectangle grassPiece = new Rectangle(pieceX, pieceY, dsize, dsize * myRand(dx, dy, i, 2, 5));
                grassPiece.setFill(randColor(dx, dy - 1));
                root.getChildren().add(grassPiece);
            }
        }
//        //花片片
            if(myRand(dx, dy, 0, 0, 5) == 0){
                int pieceX = (int) (anchor_posx + dx * size + myRand(dx, dy, 1, 1, divide - 2) * dsize);
                int pieceY = (int) (anchor_posy + dy * size + myRand(dx, dy, -1, 1, divide - 3) * dsize);
                Color yellow = Color.rgb(240, 200, 100);
                Color white = Color.rgb(255, 255, 255);
                Color green = randColor(dx, dy);
                //加深 green
                green = Color.rgb((int)(green.getRed() * 0.8 * 255), (int)(green.getGreen() * 0.8 * 255), (int)(green.getBlue() * 0.8 * 255));
                //黄色花蕊
                Rectangle f1 = new Rectangle(pieceX, pieceY, dsize, dsize);
                f1.setFill(yellow);
                root.getChildren().add(f1);
                //白色花瓣
                Rectangle f2 = new Rectangle(pieceX - dsize, pieceY, dsize, dsize);
                f2.setFill(white);
                root.getChildren().add(f2);
                Rectangle f3 = new Rectangle(pieceX + dsize, pieceY, dsize, dsize);
                f3.setFill(white);
                root.getChildren().add(f3);
                Rectangle f4 = new Rectangle(pieceX, pieceY - dsize, dsize, dsize);
                f4.setFill(white);
                root.getChildren().add(f4);
                Rectangle f5 = new Rectangle(pieceX, pieceY + dsize, dsize, dsize);
                f5.setFill(white);
                root.getChildren().add(f5);
                //绿色阴影
                Rectangle f6 = new Rectangle(pieceX, pieceY + dsize * 2, dsize, dsize);
                f6.setFill(green);
                root.getChildren().add(f6);
                Rectangle f7 = new Rectangle(pieceX + dsize, pieceY + dsize, dsize, dsize);
                f7.setFill(green);
                root.getChildren().add(f7);
                Rectangle f8 = new Rectangle(pieceX - dsize, pieceY + dsize, dsize, dsize);
                f8.setFill(green);
                root.getChildren().add(f8);
            }

    }

}
