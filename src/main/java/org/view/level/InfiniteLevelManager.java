package org.view.level;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;

import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.data.StartLobby;
import org.data.mapdata;
import org.model.*;
import org.model.Direction;

import org.view.game.box;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class InfiniteLevelManager {
    private Pane root;
    private Stage primaryStage;
    private Scene scene;
    private InfiniteLevel level;
    private User user;

    public InfiniteLevelManager(Stage primaryStage) {
        this.root = new Pane();
        this.primaryStage = primaryStage;
    }

    private void InLevel() {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            level.player.set_velocity(0, 0);
            int dx = 0, dy = 0;
            if(code == KeyCode.UP || code == KeyCode.W) {dy = -1; level.player.setOrientation(1);}
            else if(code == KeyCode.DOWN || code == KeyCode.S && !event.isControlDown()) {dy = 1; level.player.setOrientation(2);}
            else if(code == KeyCode.LEFT || code == KeyCode.A) { dx = -1; level.player.setOrientation(3);}
            else if(code == KeyCode.RIGHT || code == KeyCode.D) { dx = 1; level.player.setOrientation(4);}
            else if(code == KeyCode.R){
                level.stopTimelines();
                level.init();
                level.super_init();
            } else return;

            level.player.set_velocity(dx, dy);
            if(!level.player.is_moving){
                level.player.move(level.getMap(), level.boxes, level);
                level.player.setImageTowards(level.player.getOrientation());
            }

            level_update();
            level.drawMap();
        });

        // 根据鼠标拖动改变anchor_posx anchor_posy
        // 鼠标是否在拖动根据鼠标拖动改变anchor_posx // 同levelmanager
        AtomicBoolean isDragging = new AtomicBoolean(false);
        AtomicReference<Double> del_posx = new AtomicReference<>((double) 0);
        AtomicReference<Double> del_posy = new AtomicReference<>((double) 0);

        // 添加鼠标按下事件监听器
        scene.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                del_posx.set(level.getanchor_posx() - event.getSceneX());
                del_posy.set(level.getanchor_posy() - event.getSceneY());
            }
        });

        scene.setOnMouseDragged(event -> {
            level.player.stopCameraTimeline();
            level.setAnchor_posx(event.getSceneX() + del_posx.get());
            level.setAnchor_posy(event.getSceneY() + del_posy.get());
            level.updateAllRadiatingEffect();
            level.drawMap();
        });

        // 监听大小改变
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            // level.setAnchor_posx(level.getAnchor_posx() + (newValue.doubleValue() - oldValue.doubleValue()) / 2);
            level.getCanvas().setWidth(newValue.doubleValue());
            level.drawMap();
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            // level.setAnchor_posy(level.getAnchor_posy() + (newValue.doubleValue() - oldValue.doubleValue()) / 2);
            level.getCanvas().setHeight(newValue.doubleValue());
            level.drawMap();
        });
    }

    public void start() {
        root.getChildren().clear();
        scene = primaryStage.getScene();
        scene.setRoot(root);
        //level = new InfiniteLevel(root, StartLobby.lobbies[0], primaryStage,0, user);
        Label label = new Label("Loading...");
        label.setTextFill(Color.BLACK);
        label.setStyle("-fx-font-size: 20");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setLayoutX(config.ScreenWidth / 2 - 50);
        label.setLayoutY(config.ScreenHeight / 2 - 50);
        root.getChildren().add(label);
        FadeTransition fade = new FadeTransition(Duration.seconds(1), label);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.play();
        fade.setOnFinished(event -> {
            root.getChildren().remove(label);
            level = new InfiniteLevel(root, StartLobby.lobbies[0], primaryStage,0, user);
            level_init();
            update_box();
            level.super_init();
            InLevel();
        });
    }

    private Direction last_direction = Direction.RIGHT;
    private int last_right = 0, last_up = 0, last_down = 0, last_left = 0;
    private int RoadLength = 5, RoadWidth = 5;
    public void level_init() {
        add_level(10 + RoadLength, 3, mapdata.maps[0]);
        level.getMap().add_road(10, 3, RoadWidth, RoadLength, Direction.RIGHT);
        level.getMap().add_road(last_right,3,RoadWidth,RoadLength, Direction.RIGHT);
        level.getMap().set(10 + RoadLength,5,0);
    }

    public void win_update() {
        int new_map_id = (int) (mapdata.maps.length * Math.random());
        //Direction direction = Direction.values()[(int)(2 * Math.random()) + 1];
        Direction direction = new Direction[]{Direction.RIGHT, Direction.DOWN}[(int)(2 * Math.random())];
        int[][] next_map = mapdata.maps[new_map_id];
        change_level(direction, next_map);

        update();
    }

    private void change_level(Direction direction, int[][] data) {
        open_door(last_direction);
        add_level(last_direction, data);
        switch (direction){
            case RIGHT -> {
                level.getMap().add_road(last_right, (last_up + last_down - RoadWidth)/2, RoadWidth, RoadLength, direction);
            } case UP -> {
                level.getMap().add_road((last_left + last_right - RoadWidth)/2, last_up, RoadWidth, RoadLength, direction);
            } case DOWN -> {
                level.getMap().add_road((last_left + last_right - RoadWidth)/2, last_down, RoadWidth, RoadLength, direction);
            }
        }
        last_direction = direction;
    }

    private void open_door(Direction direction) {
        switch (direction) {
            case RIGHT -> {
                level.getMap().set(last_right - 1, (last_up + last_down)/2, 0);
            } case UP -> {
                level.getMap().set((last_right + last_left)/2, last_up, 0);
            } case DOWN -> {
                level.getMap().set((last_right +last_left)/2, last_down - 1,0);
            } case LEFT -> {

            }
        }
    }
    private void add_level(Direction direction, int[][] data) {
        switch (direction) {
            case RIGHT -> {
                int x = last_right + RoadLength;
                int y = (last_up + last_down - data.length) / 2;
                add_level(x, y, data);
                level.getMap().set(x + data.length/2, y, 0);
            } case LEFT -> {

            } case DOWN -> {
                int x = (last_right + last_left - RoadWidth) / 2;
                int y = last_down + RoadLength;
                add_level(x, y, data);
                level.getMap().set(x, y + data.length / 2,0);
            } case UP -> {
                int x = last_right + last_left - data[0].length/2;
                int y = last_up - RoadLength;
                add_level(x, y, data);
                level.getMap().set(x + data.length - 1, y + data.length / 2, 0);
            }
        }
    }

    private void add_level(int begin_x, int begin_y, int[][] data) {
        level.getMap().add_level(begin_x, begin_y, data);
        last_right = begin_x + data[0].length;
        last_down = begin_y + data.length;
        last_up = begin_y;
        last_left = begin_x;
    }

    private void update() {
        update_box();
        level.generate_glow_rects();
    }

    public void level_update() {
        if(level.isWin()){
            clear_box();
            win_update();
        }
    }

    public void clear_box() {
        level.getMap().getBox_matrix().clear();
        level.boxes.clear();

        level.getMap().getMatrixMap().keySet().stream()
                .filter(coord -> level.getMap().hasBox(coord.x, coord.y))
                .forEach(coord -> level.getMap().remove(coord.x, coord.y, 1));
    }

    public void update_box() {
        int box_index = 1; // 编号从1开始
        InfiniteMap map = level.getMap();
        if(!map.getBox_matrix().isEmpty())
            clear_box();

        List<Coordinate> keys = map.getMatrixMap().keySet()
                .stream()
                .filter(integer -> map.hasBox(integer.x, integer.y) && integer.x >= level.sublevel_begin_x && integer.y >= level.sublevel_begin_y)
                .toList();

        for(Coordinate key : keys) {
            map.setBox_matrix(key.x, key.y, box_index);
            level.boxes.add(new box(key.x, key.y, box_index));
            box_index++;
        }
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
