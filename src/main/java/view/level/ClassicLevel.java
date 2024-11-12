package view.level;

import java.util.ArrayList;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import model.MapMatrix;
import view.game.box;
import view.game.player;

public class ClassicLevel {

    private final int id;
    MapMatrix map;
    private Pane root;
    private player player;
    private ArrayList<box> boxes;

    public ClassicLevel(int id) {
        this.id = id;
        map = new MapMatrix(mapdata.maps[id]);
        this.root = new Pane();

        boxes = new ArrayList<>();
        for(int i = 0; i < map.getHeight(); i++){
            for(int j = 0; j < map.getWidth(); j++){
                if(map.hasBox(i,j)){
                    box temp = new box(i, j);
                    boxes.add(temp);
                }
                if(map.hasPlayer(i,j)){
                    player = new player(i, j);
                }
            }
        }
    }

    public void drawMap(){
        int tileSize = 50;
        for (int x = 0; x < map.getWidth(); ++x){
            for (int y = 0; y < map.getHeight(); ++y) {
                Rectangle tile = new Rectangle(y * tileSize, x * tileSize, tileSize, tileSize);

                if(map.hasNothing(x, y)) tile.setFill(Color.WHITE); // 空地
                if(map.hasWall(x,y)) tile.setFill(Color.GREY); // 墙
                if(map.hasGoal(x,y)) tile.setFill(Color.RED); // 目标
                if(map.hasBox(x,y)){
                    if(map.hasGoal(x,y))
                        tile.setFill(Color.ORANGE); // 箱子+目标
                    else
                        tile.setFill(Color.YELLOW); // 箱子
                }
                if(map.hasPlayer(x,y)){
                    if(map.hasGoal(x,y))
                        tile.setFill(Color.PURPLE); // 人+目标
                    else
                        tile.setFill(Color.BLUE); // 人
                }
                root.getChildren().add(tile);
            }
        }

    }

    public boolean isWin(){
        for(int x = 0; x < map.getWidth(); ++x)
            for(int y = 0; y < map.getHeight(); ++y)
                if(map.hasGoal(x,y) && !map.hasBox(x,y)) return false;
        return true;
    }



}
