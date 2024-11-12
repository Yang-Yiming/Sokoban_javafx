package org.view.level;

import java.util.ArrayList;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.model.MapMatrix;
import org.view.game.box;
import org.view.game.player;

public class Level {

    private final int id;
    MapMatrix map;
    private Pane root;
    player player;
    ArrayList<box> boxes;

    public void init() {
        boxes = new ArrayList<>();
        for (int y = 0; y < map.getHeight(); ++y) {
            for (int x = 0; x < map.getWidth(); ++x) {
                if (map.hasBox(x, y)) {
                    box temp = new box(x, y);
                    boxes.add(temp);
                }
                if (map.hasPlayer(x, y)) {
                    player = new player(x, y);
                }
            }
        }
        drawMap();
    }

    public Level(Pane root, int id) {
        this.root = root;
        this.id = id;
        map = new MapMatrix(mapdata.maps[id]);
        init();
    }

    public void drawMap() {
        int tileSize = 50;
        for (int y = 0; y < map.getHeight(); ++y) {
            for (int x = 0; x < map.getWidth(); ++x) {
                Rectangle tile = new Rectangle(x * tileSize, y * tileSize, tileSize, tileSize);
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

}
