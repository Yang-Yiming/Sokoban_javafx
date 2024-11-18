package org.view.level;

import java.util.ArrayList;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


import org.model.MapMatrix;
import org.model.config;
import org.view.game.box;
import org.view.game.player;

public class Level {

    private final int id;
    MapMatrix map;
    private Pane root;
    player player;
    ArrayList<box> boxes;

    private ArrayList<Rectangle> glowRectangles;

    // 从此处开始绘制 // 这可真是依托史山啊
    private double anchor_posx;
    private double anchor_posy;

    public void init() {
        map = new MapMatrix(mapdata.maps[id]);
        glowRectangles = new ArrayList<>();

        int boxIndex = 1; // 从1开始编号
        boxes = new ArrayList<>();

        setAnchor_posx((double)(config.ScreenWidth - map.getWidth() * config.tile_size)/2);
        setAnchor_posy((double)(config.ScreenHeight - map.getHeight() * config.tile_size)/2);

        for (int y = 0; y < map.getHeight(); ++y) {
            for (int x = 0; x < map.getWidth(); ++x) {
                if (map.hasBox(x, y)) {
                    box temp = new box(x, y, boxIndex++);
                    temp.getImageView().setX(anchor_posx + x * config.tile_size);
                    temp.getImageView().setY(anchor_posy + y * config.tile_size);
                    boxes.add(temp);
                }
                if (map.hasPlayer(x, y)) {
                    player = new player(x, y);
                    player.getImageView().setX(anchor_posx + x * config.tile_size);
                    player.getImageView().setY(anchor_posy + y * config.tile_size);
                    // System.out.println("x : " + x + ", y : " + y);
                }
            }
        }
        for (int y = 0; y < map.getHeight(); ++y) {
            for (int x = 0; x < map.getWidth(); ++x) {
                if (map.hasGoal(x, y)) {
                    createRadiatingEffect(x, y, config.tile_size);
                }
            }
        }
        drawMap();
    }

    public Level(Pane root, int id) {
        this.root = root;
        this.id = id;
        init();
    }

    public void drawBackGround() {
        int tileSize = config.tile_size;
        for (int y = 0; y < map.getHeight(); ++y) {
            for (int x = 0; x < map.getWidth(); ++x) {
                double posx = anchor_posx + x * tileSize;
                double posy = anchor_posy + y * tileSize;

//                if (map.hasNothing(x, y)) {
//                    Rectangle tile = new Rectangle(posx, posy, tileSize, tileSize);
//                    tile.setFill(Color.GREY); // 空地
//                    root.getChildren().add(tile);}
                if (map.hasWall(x, y)) {
                    ImageView wall = new ImageView(new Image(getClass().getResourceAsStream("/images/wall.bmp")));
                    wall.setFitWidth(tileSize);
                    wall.setFitHeight(tileSize);
                    wall.setX(posx);
                    wall.setY(posy);
                    root.getChildren().add(wall);
                } else if (map.hasGoal(x, y)) {
                    ImageView goal = new ImageView(new Image(getClass().getResourceAsStream("/images/goal.png")));
                    goal.setFitWidth(tileSize);
                    goal.setFitHeight(tileSize);
                    goal.setX(posx);
                    goal.setY(posy);
                    root.getChildren().add(goal);
                } else {
                    Rectangle tile = new Rectangle(posx, posy, tileSize, tileSize);
                    tile.setFill(Color.GREY); // 空地
                    root.getChildren().add(tile);
                }
            }
        }
        //将所有 glowrectangles 里的成员置于图层最上方
        for (Rectangle rect : glowRectangles) {
            root.getChildren().add(rect);
        }
    }
    private void createRadiatingEffect(int x, int y, int tileSize) { //需要传实时的数据。
        double lowLimit = 0.9, highLimit = 1.2;
        double initialCenterX = anchor_posx + x * tileSize;
        double initialCenterY = anchor_posy + y * tileSize;
        Rectangle rect = new Rectangle(initialCenterX, initialCenterY, tileSize * lowLimit, tileSize * lowLimit);
        rect.setX(initialCenterX - (rect.getWidth() - tileSize) / 2);
        rect.setY(initialCenterY - (rect.getHeight() - tileSize) / 2);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.WHITE);
        rect.setStrokeWidth(2);
        // root.getChildren().add(rect);
        glowRectangles.add(rect);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.04), e -> {
            if(rect.getWidth() > tileSize * highLimit) {
                rect.setWidth(tileSize * lowLimit);
                rect.setHeight(tileSize * lowLimit);
            }
            //正方形逐渐放大
            rect.setWidth(rect.getWidth() + 1);
            rect.setHeight(rect.getHeight() + 1);
            //正方形居中
            double centerX = anchor_posx + x * tileSize;
            double centerY = anchor_posy + y * tileSize;
            rect.setX(centerX - (rect.getWidth() - tileSize) / 2);
            rect.setY(centerY - (rect.getHeight() - tileSize) / 2);
            //正方形逐渐变淡
            rect.setStroke(Color.rgb(255, 255, 255, 1 - (rect.getWidth() - tileSize * lowLimit) / (tileSize * (highLimit - lowLimit))));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void drawBoxes() {
        for(box box : boxes) {
//            double posx = anchor_posx + box.get_x() * config.tile_size;
//            double posy = anchor_posy + box.get_y() * config.tile_size;
            ImageView boxview = box.getImageView();
//            boxview.setX(posx);
//            boxview.setY(posy);
            root.getChildren().add(boxview);
        }
    }
    public void drawPlayer() {
        ImageView playerview = player.getImageView();
        playerview.setX(playerview.getX() + anchor_posx);
        playerview.setY(playerview.getY() + anchor_posy);
        root.getChildren().add(player.getImageView());
        playerview.setX(playerview.getX() - anchor_posx);
        playerview.setY(playerview.getY() - anchor_posy);
    }

    public void drawMap() {
        root.getChildren().clear(); // 先清空一下地图

        drawBackGround();
        drawBoxes();
        drawPlayer();
    }

    public boolean isWin() {
        for (int y = 0; y < map.getHeight(); ++y)
            for (int x = 0; x < map.getWidth(); ++x)
                if (map.hasGoal(x, y) && !map.hasBox(x, y))
                    return false;
        return true;
    }

//    public void moveBox(box box, int dx, int dy) {
//        TranslateTransition transition = new TranslateTransition(Duration.millis(200), box.getImageView());
//        transition.setByX(dx * config.tile_size);
//        transition.setByY(dy * config.tile_size);
//        transition.play();
//    }
//
//    public void movePlayer(int dx, int dy) {
//        TranslateTransition transition = new TranslateTransition(Duration.millis(200), player.getImageView());
//
//        transition.setFromX((player.get_x() - 2*dx) * config.tile_size);
//        transition.setFromY((player.get_y() - 2*dy) * config.tile_size);
//        transition.setToX((player.get_x() - 1*dx) * config.tile_size);
//        transition.setToY((player.get_y() - 1*dy) * config.tile_size);
//
//        transition.play();
//    }

    // getter setter
    public double getanchor_posx() {
        return anchor_posx;
    }
    public double getanchor_posy() {
        return anchor_posy;
    }
    public void setAnchor_posx(double anchor_posx) {
        this.anchor_posx = anchor_posx;
    }
    public void setAnchor_posy(double anchor_posy) {
        this.anchor_posy = anchor_posy;
    }

}
