package org.view.LevelSelect;

import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.model.*;

import java.util.ArrayList;
import java.util.HashMap;

class Cat {
    Image cat_stand = new Image(getClass().getResourceAsStream("/images/player_cat/cat_stand.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image cat_run = new Image(getClass().getResourceAsStream("/images/player_cat/cat_run.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image cat_run_front = new Image(getClass().getResourceAsStream("/images/player_cat/cat_run_front.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image cat_run_back = new Image(getClass().getResourceAsStream("/images/player_cat/cat_run_back.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image cat_stand_front = new Image(getClass().getResourceAsStream("/images/player_cat/cat_stand_front.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image cat_stand_back = new Image(getClass().getResourceAsStream("/images/player_cat/cat_stand_back.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    ImageView imageView;
    int x, y;
    public Cat() {
        imageView = new ImageView(cat_stand);
        imageView.setFitHeight(config.Map_Node_Width);
        imageView.setFitWidth(config.Map_Node_Width);
        x = 0; y = 0;
    }
    boolean is_moving = false;
    public void move(char dir){ // 0 : right 1:down 2:up
        imageView.setFitHeight(config.Map_Node_Width); imageView.setFitWidth(config.Map_Node_Width);
        int dx = 0, dy = 0;

        if(dir == 'd') {
            imageView.setImage(cat_run); imageView.setScaleX(1);
            dx = 1;
        } else if (dir == 's'){
            imageView.setImage(cat_run_front);
            dy = 1;
        } else if (dir == 'w'){
            imageView.setImage(cat_run_back);
            dy = -1;
        } else if (dir == 'a'){
            imageView.setImage(cat_run); imageView.setScaleX(-1);
            dx = -1;
        }
        x += dx; y += dy;

        Timeline move_timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(imageView.layoutXProperty(), imageView.getLayoutX()),
                        new KeyValue(imageView.layoutYProperty(), imageView.getLayoutY())),
                new KeyFrame(Duration.millis(config.move_anim_duration),
                        new KeyValue(imageView.layoutXProperty(), SelectMap.AnchorX + x * config.Map_Node_Width),
                        new KeyValue(imageView.layoutYProperty(), SelectMap.AnchorY + y * config.Map_Node_Width))
        );
        move_timeline.setOnFinished(e -> {
            if(dir == 'd') {imageView.setImage(cat_stand); imageView.setScaleX(1);}
            else if(dir == 's') imageView.setImage(cat_stand_front);
            else if(dir == 'w') imageView.setImage(cat_stand_back);
            else if(dir == 'a') {imageView.setImage(cat_stand); imageView.setScaleX(-1);}
            imageView.setFitHeight(config.Map_Node_Width); imageView.setFitWidth(config.Map_Node_Width);
            is_moving = false;
            imageView.setTranslateX(0); imageView.setTranslateY(0);
            imageView.setLayoutX(SelectMap.AnchorX + x * config.Map_Node_Width);
            imageView.setLayoutY(SelectMap.AnchorY + y * config.Map_Node_Width);
        });
        is_moving = true;

        move_timeline.setCycleCount(1);
        move_timeline.play();
    }
}


public class SelectMap {
    static double AnchorX, AnchorY;
    private Stage stage;
    private Scene scene;
    private Pane root;
    private Cat cat;
    private HashMap<Coordinate, Integer> map;

    private ArrayList<MapNode> nodes;
    private ArrayList<Chest> chests;

    public SelectMap(Stage stage) {
        this.stage = stage;
        cat = new Cat();
        map = new HashMap<>();
        if(stage.getScene() != null){
            this.scene = stage.getScene();
            this.root = (Pane) scene.getRoot();
        } else {
            this.root = new Pane();
            this.scene = new Scene(root);
        }
        AnchorX = scene.getWidth() / 2 - 100; AnchorY = (scene.getHeight() - config.Map_Node_Width) / 2;
    }

    int count(int x, int y, int goal) {
        int cnt = 0;
        int[] dx = new int[]{1, 0, -1, 0, 1, 1, -1, -1};
        int[] dy = new int[]{0, 1, 0, -1, 1, -1, 1, -1};
        for(int i = 0; i < 8; i++) {
            int xx = x + dx[i], yy = y + dy[i];
            if(xx == 0 && yy == 0) return -1;
            int get = map.getOrDefault(new Coordinate(xx, yy), 0);
            if(get == goal) cnt++;
            if(get == -1 || get > 0) return -1; // 让目标点和宝箱周围无障碍
        }
        return cnt;
    }

    int count1(int x, int y, int goal) {
        int cnt = 0;
        int[] dx = new int[]{1, 0, -1, 0};
        int[] dy = new int[]{0, 1, 0, -1};
        for(int i = 0; i < 4; i++) {
            int xx = x + dx[i], yy = y + dy[i];
            if(xx == 0 && yy == 0) return -1;
            int get = map.getOrDefault(new Coordinate(xx, yy), 0);
            if(get == goal) cnt++;
            if(get == -1 || get > 0) return -1; // 让目标点和宝箱周围无障碍
        }
        return cnt;
    }

    int count2(int x, int y, int goal) {
        int cnt = 0;
        int[] dx = new int[]{2, 0, -2, 0, 1, 1, -1, -1};
        int[] dy = new int[]{0, 2, 0, -2, 1, -1, 1, -1};
        for(int i = 0; i < 8; i++) {
            int xx = x + dx[i], yy = y + dy[i];
            if(xx == 0 && yy == 0) return -1;
            int get = map.getOrDefault(new Coordinate(xx, yy), 0);
            if(get == goal) cnt++;
            if(get == -1 || get > 0) return -1; // 让目标点和宝箱周围无障碍
        }
        return cnt;
    }


    final int WATER = 70, ROCK = 25;
    void generate_obstacle(int begin_x, int begin_y, int end_x, int end_y, int times) {
//        if(true) return;
        for(int i = 0; i < WATER; i++) {
            int x = (int)(Math.random() * (end_x - begin_x)) + begin_x;
            int y = (int)(Math.random() * (end_y - begin_y)) + begin_y;
            if(map.containsKey(new Coordinate(x, y))) continue;
            map.put(new Coordinate(x, y), -3);
        }
        for(int i = 0; i < ROCK; i++) {
            int x = (int)(Math.random() * (end_x - begin_x)) + begin_x;
            int y = (int)(Math.random() * (end_y - begin_y)) + begin_y;
            if(map.containsKey(new Coordinate(x, y))) continue;
            map.put(new Coordinate(x,y), -2);
        }

        while(times --> 0) { // 时间趋近于0
            for(int xx = begin_x; xx < end_x; xx++) {
                for(int yy = begin_y; yy < end_y; yy++) {
                    Coordinate now = new Coordinate(xx, yy);
                    if(map.getOrDefault(now,0) == -1 || map.getOrDefault(now, 0)>0)
                        continue;

                    int cnt_water = count(xx, yy, -3);
                    int cnt_rock = count(xx, yy, -2);
                    if(cnt_rock < 0 || cnt_water < 0){
                        map.remove(now);
                        continue;
                    }
                    if (cnt_water <= 1 && map.getOrDefault(now, 0) == -3) {
                        map.remove(now);
                    }

                    if(times >= 2) { // 最后几次用来清理
                        if (cnt_water >= 3 && Math.random() < 0.3) {
                            map.put(now, -3);
                        }
                        if (cnt_rock >= 5 && Math.random() < 0.7) {
                            map.put(now, -2);
                        }
                    }
                }
            }
//            random_remove(begin_x, 0, end_x, MapNode.YRange, 10);
        }
    }
    void remove_all(int begin_x, int begin_y, int end_x, int end_y) {
        for(int i = begin_x; i < end_x; i++) {
            for(int j = begin_y; j < end_y; j++) {
                map.remove(new Coordinate(i, j));
            }
        }
    }
    void random_remove(int begin_x, int begin_y, int end_x, int end_y, int NUM) {
        for(int i = 0; i < NUM; i++) {
            int x = (int)(Math.random() * (end_x - begin_x)) + begin_x;
            int y = (int)(Math.random() * (end_y - begin_y)) + begin_y;
            map.remove(new Coordinate(x, y));
        }
    }

    void fill_all(int begin_x, int begin_y, int end_x, int end_y, int item) {
        for(int i = begin_x; i < end_x; i++) {
            for(int j = begin_y; j < end_y; j++) {
                map.put(new Coordinate(i, j), item);
            }
        }
    }

    public void add_levels(int[][][] maps, User user) {
        // 关卡
        MapNode.maps = maps;
        for (int i = 0; i < maps.length; i++) {
            MapNode node = new MapNode(i, stage);
            node.target_level = i;
            if (user.getMaxLevel() < i) node.is_locked = true;
            if (nodes == null) nodes = new ArrayList<>();
            nodes.add(node);
            map.put(new Coordinate(node.x, node.y), i + 1);
        }

        // 宝箱
        if(chests == null) chests = new ArrayList<>();
        chests.add(new Chest(2,2, root));
        map.put(new Coordinate(2, 2), -1);

        // 障碍
        int left_x = -(int)(AnchorX / config.Map_Node_Width);
        int right_x = (int)((scene.getWidth() - AnchorX) / config.Map_Node_Width);
        int up_y = -(int)(AnchorY / config.Map_Node_Width);
        int down_y = (int)((scene.getHeight() - AnchorY) / config.Map_Node_Width);

        generate_obstacle(left_x-1, up_y-1, right_x-1, down_y-1, 20);
    }

    public void draw() {
        root.getChildren().clear(); root.setStyle("-fx-background-color: #7C9920");
        scene.getStylesheets().add("file://" + new java.io.File("./src/main/resources/css/styles.css").getAbsolutePath());
        root.setLayoutX(0); root.setLayoutY(0); // 设置根节点的位置

        //画背景
        Rectangle background = new Rectangle(0,0,config.ScreenWidth, config.ScreenHeight);
        background.setFill(Color.WHITE);
        root.getChildren().add(background);

        // 画障碍
        for(Coordinate c: map.keySet()) {
            // rock ?
            if(map.get(c) == -2) {
                Rectangle rect = new Rectangle(AnchorX + c.x * config.Map_Node_Width, AnchorY + c.y * config.Map_Node_Width, config.Map_Node_Width, config.Map_Node_Width);
                rect.setFill(Color.BLACK);
                root.getChildren().add(rect);
            }
            // water ?
            Color deepBlue = Color.web("4c6e78");
            Color blue = Color.web("5d9798");
            Color lightBlue = Color.web("77ad9d");
            if(map.get(c) == -3) {
                Rectangle rect = new Rectangle(AnchorX + c.x * config.Map_Node_Width, AnchorY + c.y * config.Map_Node_Width, config.Map_Node_Width, config.Map_Node_Width);
                //如果在边界上，就是深蓝色
                if(count1(c.x, c.y, -3) < 4) rect.setFill(deepBlue);
                else if(count2(c.x, c.y, -3) < 8) rect.setFill(blue);
                else rect.setFill(lightBlue);
                root.getChildren().add(rect);
            }
        }

        // 画宝箱
        for(Chest chest: chests) {
            chest.imageView.setLayoutX(AnchorX + chest.x * config.Map_Node_Width);
            chest.imageView.setLayoutY(AnchorY + chest.y * config.Map_Node_Width);
            root.getChildren().add(chest.imageView);
        }

        // 画goals
        for(MapNode node: nodes) {
            node.stackPane.setLayoutX(AnchorX + node.x * config.Map_Node_Width);
            node.stackPane.setLayoutY(AnchorY + node.y * config.Map_Node_Width);
            root.getChildren().add(node.stackPane);
        }
        // 画猫
        cat.imageView.setLayoutX(AnchorX + cat.x * config.Map_Node_Width);
        cat.imageView.setLayoutY(AnchorY + cat.y * config.Map_Node_Width);
        root.getChildren().add(cat.imageView);
    }

    Timeline cameraTimeline = null;

    public void Move() {
        cameraTimeline = new Timeline(new KeyFrame(Duration.seconds(0.03), e -> {
            // 得到画面中心的坐标
            double midx = stage.getWidth() / 2 - (double) config.Map_Node_Width / 2;
            double midy = stage.getHeight() / 2 - (double) config.Map_Node_Width / 2;
            // 得到中心的坐标
            double catx = cat.imageView.getLayoutX() + config.Map_Node_Width / 2;
            double caty = cat.imageView.getLayoutY() + config.Map_Node_Width/ 2;
            double dx = midx - catx, dy = midy - caty;
            int mid_dis = 3;
            if(dx < -config.Map_Node_Width * mid_dis) dx += config.Map_Node_Width * mid_dis;
            else if(dx > config.Map_Node_Width * mid_dis) dx -= config.Map_Node_Width * mid_dis;
            else dx = 0;
            if(dy < -config.Map_Node_Width * mid_dis) dy += config.Map_Node_Width * mid_dis;
            else if(dy > config.Map_Node_Width * mid_dis) dy -= config.Map_Node_Width * mid_dis;
            else dy = 0;
            //if(Math.abs(dx) < 10 && Math.abs(dy) < 10 && cameraTimeline != null) cameraTimeline.stop();
            // 移动画面，使人物在中间
            AnchorX += dx / 30; AnchorY += dy / 30;
            draw();
        }));
        config.timelines.add(cameraTimeline);
        cameraTimeline.setCycleCount(Timeline.INDEFINITE);
        cameraTimeline.play();
    }

    public void update() {
        draw();
        Move();
        String[] moves = {""};
        Chest[] goal_chest = new Chest[]{null};

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(config.move_anim_duration ), e->{
            if (!cat.is_moving && !moves[0].isEmpty()) {
                char dir = moves[0].charAt(0);
                moves[0] = moves[0].substring(1);
                cat.move(dir);
            }
            if (moves[0].isEmpty()){
                int at = map.getOrDefault(new Coordinate(cat.x, cat.y),0);
                if(at > 0 && !nodes.get(at - 1).is_locked) {
                    nodes.get(at - 1).action();
                    map.clear();
                    nodes = null;
                    cameraTimeline.stop();
                }
                if(goal_chest[0] != null) {
                    goal_chest[0].open();
                    goal_chest[0] = null;
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        scene.setOnMouseClicked(event -> {
            if(event.getClickCount() != 1)  return;

            int x = (int) ((event.getSceneX() - AnchorX) / config.Map_Node_Width);
            int y = (int) ((event.getSceneY() - AnchorY) / config.Map_Node_Width);

            if(map.getOrDefault(new Coordinate(x,y),0) < -1){
                return;
            }
            // 对箱子的特殊处理
            if(map.getOrDefault(new Coordinate(x,y),0) == -1) {
                for(Chest chest: chests) {
                    if(chest.x == x && chest.y == y) {
                        goal_chest[0] = chest;
                        break;
                    }
                }
                Coordinate goal = new Coordinate(x, y);
                map.put(goal, 0);
                moves[0] = FindPath.findPath(map, new Coordinate(cat.x, cat.y), goal);
                map.put(goal, -1);
                moves[0] = moves[0].substring(0, moves[0].length() - 1);
            } else{
                moves[0] = FindPath.findPath(map, new Coordinate(cat.x, cat.y), new Coordinate(x, y));
            }
            timeline.play();
        });

    }

    public Scene getScene() {
        return scene;
    }
}
