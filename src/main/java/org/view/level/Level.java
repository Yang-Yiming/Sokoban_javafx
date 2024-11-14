package org.view.level;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
    public int[][] boxMatrix;
    int boxIndex;

    // 从此处开始绘制 // 这可真是依托史山啊
    private double anchor_posx;
    private double anchor_posy;

    public void init() {
        map = new MapMatrix(mapdata.maps[id]);

        boxIndex = 1; // 从1开始编号
        boxes = new ArrayList<>();

        setAnchor_posx((double)(config.ScreenWidth - map.getWidth() * config.tile_size)/2);
        setAnchor_posy((double)(config.ScreenHeight - map.getHeight() * config.tile_size)/2);

        for (int y = 0; y < map.getHeight(); ++y) {
            for (int x = 0; x < map.getWidth(); ++x) {
                if (map.hasBox(x, y)) {
                    box temp = new box(x, y, boxIndex++);
                    boxes.add(temp);
                }
                if (map.hasPlayer(x, y)) {
                    player = new player(x, y);
                    // System.out.println("x : " + x + ", y : " + y);
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
                double posx = anchor_posx + x * tileSize; double posy = anchor_posy + y * tileSize;

                if (map.hasNothing(x, y)){
                    Rectangle tile = new Rectangle(posx, posy, tileSize, tileSize);
                    tile.setFill(Color.GREY); // 空地
                    root.getChildren().add(tile);
                }
                if (map.hasWall(x, y)){
                    ImageView wall = new ImageView(new Image(getClass().getResourceAsStream("/images/wall.bmp")));
                    wall.setFitWidth(tileSize); wall.setFitHeight(tileSize);
                    wall.setX(posx); wall.setY(posy);
                    root.getChildren().add(wall);
                }

                if (map.hasGoal(x, y)){
                    ImageView goal = new ImageView(new Image(getClass().getResourceAsStream("/images/goal.png")));
                    goal.setFitWidth(tileSize); goal.setFitHeight(tileSize);
                    goal.setX(posx); goal.setY(posy);
                    root.getChildren().add(goal);
                }
            }
        }
    }

    public void drawBoxes() {
        for(box box : boxes) {
            double posx = anchor_posx + box.get_x() * config.tile_size;
            double posy = anchor_posy + box.get_y() * config.tile_size;
            ImageView boxview = box.getImageView();
            boxview.setX(posx); boxview.setY(posy);
            root.getChildren().add(boxview);
        }
    }
    public void drawPlayer() {
        ImageView playerview = new ImageView(player.getImage());
        playerview.setX(anchor_posx + player.get_x() * config.tile_size);
        playerview.setY(anchor_posy + player.get_y() * config.tile_size);
        root.getChildren().add(playerview);
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
