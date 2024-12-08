package org.view.level;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.data.mapdata;
import org.model.MapMatrix;
import org.model.User;
import org.model.config;
import org.view.VisualEffects.GlowRectangle;
import org.view.game.box;
import org.view.game.player;

import java.util.ArrayList;

public class FightLevel extends Level {

    private final int id;
    private boolean reverse = false;


    public MapMatrix getMap() {
        return (MapMatrix) map;
    }
    org.view.game.player player1, player2;

    public FightLevel(Pane root, int id, Stage primaryStage, User user, boolean reverse) {
        super(root, primaryStage, user);
        this.id = id;
        this.reverse = reverse;
        //将 mapdata.maps[id] 对称复制一份，然后赋值给 map
        int[][] doubleMap = new int[mapdata.maps[id].length][mapdata.maps[id][0].length * 2 + 1];
        for(int i = 0; i < mapdata.maps[id].length; i++){
            for(int j = 0; j < mapdata.maps[id][0].length; j++){
                doubleMap[i][j] = mapdata.maps[id][i][j];
                doubleMap[i][mapdata.maps[id][0].length * 2 - j] = mapdata.maps[id][i][j];
            }
        }
        map = new MapMatrix(doubleMap);
        init();
    }
    @Override
    public void init() {
        if(canvas == null)
            canvas = new Canvas(primaryStage.getWidth(), primaryStage.getHeight());

        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int boxIndex = 1; // 从1开始编号
        boxes = new ArrayList<>();

        double width = primaryStage.getWidth();
        double height = primaryStage.getHeight();

        setAnchor_posx((width - map.getWidth() * config.tile_size)/2);
        setAnchor_posy((height - map.getHeight() * config.tile_size)/2);

        for (int y = 0; y < map.getHeight(); ++y) {
            for (int x = 0; x < map.getWidth(); ++x) {
                if (map.hasBox(x, y)) {
                    box temp = new box(x, y, boxIndex++);
                    temp.getImageView().setX(anchor_posx + x * config.tile_size);
                    temp.getImageView().setY(anchor_posy + (y - config.box_angle_amount) * config.tile_size);
                    boxes.add(temp);
                }
                if (map.hasPlayer(x, y)) {
                    if(player1 == null){
                        player1 = new player(x, y, primaryStage, true);
                        player1.getImageView().setX(anchor_posx + x * config.tile_size);
                        player1.getImageView().setY(anchor_posy + y * config.tile_size);
                    }else{
                        player2 = new player(x, y, primaryStage, true);
                        player2.getImageView().setX(anchor_posx + x * config.tile_size);
                        player2.getImageView().setY(anchor_posy + y * config.tile_size);
                    }
                }
            }
        }
        generate_glow_rects();
        createButterflyTimeline();
        load_gui(user);
        fadeRectangle = new Rectangle(primaryStage.getWidth(), primaryStage.getHeight(), Color.BLACK);
        fadeRectangle.setX(0);
        fadeRectangle.setY(0);
        fadeRectangle.setOpacity(1.0);
        root.getChildren().add(fadeRectangle);
        createFadeTimeline();
        drawMap();
    }
    public void drawPlayer() {
        root.getChildren().add(player1.getImageView());
        root.getChildren().add(player2.getImageView());
    }

    public int oneIsWin(){
        boolean player1Win = true, player2Win = true;
        for(int y = 0; y < map.getHeight(); y++){
            for(int x = 0; x < map.getWidth() / 2; x++){
                if(map.hasGoal(x, y) && !map.hasBox(x, y)){
                    player1Win = false;
                    break;
                }
            }
        }
        for(int y = 0; y < map.getHeight(); y++){
            for(int x = map.getWidth() / 2; x < map.getWidth(); x++){
                if(map.hasGoal(x, y) && !map.hasBox(x, y)){
                    player2Win = false;
                    break;
                }
            }
        }
        if(player1Win) return 1;
        if(player2Win) return 2;
        return 0;
    }

    @Override
    public void stopTimelines(){
        butterflyTimeline.stop();
        this.player1.stopCameraTimeline();
        this.player2.stopCameraTimeline();
        GlowRectangle.timeline.stop();
    }

}
