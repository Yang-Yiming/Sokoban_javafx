package org.view.level;

import java.util.ArrayList;

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

    public void drawMap() {
        root.getChildren().clear(); // 先清空一下地图

        for (int y = 0; y < map.getHeight(); ++y) {
            int tileSize = config.tile_size;
            for (int x = 0; x < map.getWidth(); ++x) {
                Rectangle tile = new Rectangle(anchor_posx + x * tileSize, anchor_posy + y * tileSize, tileSize, tileSize);
                // System.out.println("x : " + x + ", y : " + y);
                if (map.hasNothing(x, y))
                    tile.setFill(Color.WHITE); // 空地
                if (map.hasWall(x, y))
                    tile.setFill(Color.GREY); // 墙
                if (map.hasGoal(x, y))
                    tile.setFill(Color.RED); // 目标
                if (map.hasBox(x, y)) {
                    if (map.hasGoal(x, y))
                        tile.setFill(Color.ORANGE); // 箱子+目标
                    else
                        tile.setFill(Color.YELLOW); // 箱子
                }
                if (map.hasPlayer(x, y)) {
                    if (map.hasGoal(x, y))
                        tile.setFill(Color.PURPLE); // 人+目标
                    else
                        tile.setFill(Color.BLUE); // 人
                }
                root.getChildren().add(tile);
            }
        }

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
