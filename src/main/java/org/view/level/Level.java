package org.view.level;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.data.mapdata;
import org.model.*;
import org.view.game.box;
import org.view.game.player;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Level {

    protected GameMap map;
    
    protected Pane root;
    org.view.game.player player;
    ArrayList<box> boxes;
    private Stage primaryStage;

    private ArrayList<Rectangle> glowRectangles;
    private Rectangle[][] radiatingEffects;

    private Canvas canvas; // 用来放grass

    private GUIController guiController; // 显示gui
    private Pane gui_root; // gui的root
    protected User user; // 史山

    // 从此处开始绘制 // 这可真是依托史山啊
    private double anchor_posx;
    private double anchor_posy;

    public void init() {
        glowTimelines.clear();
        radiatingEffects = new Rectangle[map.getHeight()][map.getWidth()];
        glowRectangles = new ArrayList<>();
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
                    temp.getImageView().setY(anchor_posy + y * config.tile_size);
                    boxes.add(temp);
                }
                if (map.hasPlayer(x, y)) {
                    player = new player(x, y, primaryStage);
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
        createButterflyTimeline();
        load_gui(user);
        drawMap();
    }
    private Timeline butterflyTimeline = null;
    public void createButterflyTimeline(){
        butterflyTimeline = new Timeline(new KeyFrame(Duration.seconds(0.08), e -> {
            Grass.updateTimeid();
            drawMap();
        }));
        butterflyTimeline.setCycleCount(Animation.INDEFINITE);
        butterflyTimeline.play();
    }
    public void stopTimelines(){
        butterflyTimeline.stop();
        for(Timeline timeline : glowTimelines){
            timeline.stop();
        }
        this.player.stopCameraTimeline();
    }

    public Level(Pane root, Stage primaryStage, User user) {
        this.root = root;
        this.primaryStage = primaryStage;
        if(canvas == null)
            canvas = new Canvas(primaryStage.getWidth(), primaryStage.getHeight());
        this.user = user;
        //load_gui(user);
    }

    protected void load_gui(User user) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GUI.fxml"));
        try {
            gui_root = loader.load();
            guiController = loader.getController();
        } catch (IOException e) {
            System.out.println("Failed to load FXML file: " + e.getMessage());
            throw new RuntimeException(e);
        }
        this.user = user;
    }

    public void drawGrass(){
        root.getChildren().add(canvas);
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //将能显示在窗口里的部分都用 grass 填充
        double width = primaryStage.getWidth();
        double height = primaryStage.getHeight();
        int leftNum = (int) Math.ceil(anchor_posx / config.tile_size);
        int rightNum = (int) Math.ceil((width - anchor_posx) / config.tile_size);
        int upNum = (int) Math.ceil(anchor_posy / config.tile_size);
        int downNum = (int) Math.ceil((height - anchor_posy) / config.tile_size);
        for(int dx = -leftNum; dx < map.getWidth() + rightNum; ++dx)
            for(int dy = -upNum; dy < map.getHeight() + downNum; ++dy)
                Grass.addGrass(canvas, dx, dy, anchor_posx, anchor_posy, config.tile_size);
    }
    public void drawButterfly(){
        double width = primaryStage.getWidth();
        double height = primaryStage.getHeight();
        int leftNum = (int) Math.ceil(anchor_posx / config.tile_size);
        int rightNum = (int) Math.ceil((width - anchor_posx) / config.tile_size);
        int upNum = (int) Math.ceil(anchor_posy / config.tile_size);
        int downNum = (int) Math.ceil((height - anchor_posy) / config.tile_size);
        for(int dx = -leftNum; dx < map.getWidth() + rightNum; ++dx)
            for(int dy = -upNum; dy < map.getHeight() + downNum; ++dy)
                Grass.addButterfly(root, dx, dy, anchor_posx, anchor_posy, config.tile_size);
    }
    public void drawButterflyShadow(){
        double width = primaryStage.getWidth();
        double height = primaryStage.getHeight();
        int leftNum = (int) Math.ceil(anchor_posx / config.tile_size);
        int rightNum = (int) Math.ceil((width - anchor_posx) / config.tile_size);
        int upNum = (int) Math.ceil(anchor_posy / config.tile_size);
        int downNum = (int) Math.ceil((height - anchor_posy) / config.tile_size);
        for(int dx = -leftNum; dx < map.getWidth() + rightNum; ++dx)
            for(int dy = -upNum; dy < map.getHeight() + downNum; ++dy)
                Grass.addButterflyShadow(root, dx, dy, anchor_posx, anchor_posy, config.tile_size);
    }
    public void drawBackGround(int begin_x, int begin_y) {
        int tileSize = config.tile_size;
        for (int y = begin_y; y < begin_y + map.getHeight(); ++y) {
            for (int x = begin_x; x < begin_x + map.getWidth(); ++x) {
                double posx = anchor_posx + x * tileSize;
                double posy = anchor_posy + y * tileSize;

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
                }
//                else {
//                    Rectangle tile = n.add(tile);ew Rectangle(posx, posy, tileSize, tileSize);
////                    tile.setFill(Color.GREY); // 空地
////                    root.getChildren()
//                }
            }
        }
        //将所有 glowrectangles 里的成员置于图层最上方
        for (Rectangle rect : glowRectangles) {
            root.getChildren().add(rect);
        }
    }
    public void updateAllRadiatingEffect() {
        for (int y = 0; y < map.getHeight(); ++y) {
            for (int x = 0; x < map.getWidth(); ++x) {
                if (map.hasGoal(x, y)) {
                    updateRectangle(radiatingEffects[y][x], config.tile_size, x, y);
                }
            }
        }
    }
    private static final double lowLimit = 0.9;
    private static final double highLimit = 1.2;
    private ArrayList<Timeline> glowTimelines = new ArrayList<>();
    private void createRadiatingEffect(int x, int y, int tileSize) { //需要传实时的数据。
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
        radiatingEffects[y][x] = rect;
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), e -> {
            if(rect.getWidth() > tileSize * highLimit) {
                rect.setWidth(tileSize * lowLimit);
                rect.setHeight(tileSize * lowLimit);
            }
            // 正方形逐渐放大
            rect.setWidth(rect.getWidth() + 1);
            rect.setHeight(rect.getHeight() + 1);
            // 正方形居中
            double centerX = anchor_posx + x * tileSize;
            double centerY = anchor_posy + y * tileSize;
            rect.setX(centerX - (rect.getWidth() - tileSize) / 2);
            rect.setY(centerY - (rect.getHeight() - tileSize) / 2);
            // 正方形逐渐变淡
            double v = 1 - (rect.getWidth() - tileSize * lowLimit) / (tileSize * (highLimit - lowLimit));
            rect.setStroke(Color.rgb(255, 255, 255, Math.max(v,0))); // 小于0会导致一大拖报错
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        glowTimelines.add(timeline);
    }
    private void updateRectangle(Rectangle rect, int tileSize, int x, int y) {
        double centerX = anchor_posx + x * tileSize;
        double centerY = anchor_posy + y * tileSize;
        rect.setX(centerX - (rect.getWidth() - tileSize) / 2);
        rect.setY(centerY - (rect.getHeight() - tileSize) / 2);
    }
    /*
     * 开一个数组存放所有的 glowRectangles
     * 移动鼠标的时候，更新所有的 glowRectangles
     * 通过 Timeline 来实现动画
     * */
    public void drawBoxes() {
        for(box box : boxes) {
            root.getChildren().add(box.getImageView());
        }
    }
    public void drawPlayer() {
        root.getChildren().add(player.getImageView());
    }

    public void drawGUI() {
        if(user == null) return;
        guiController.update(user.getMoveCount());
        root.getChildren().addAll(gui_root);
    }

    public void drawMap() {
        root.getChildren().clear(); // 先清空一下地图
        drawGrass();
        drawButterflyShadow();
        drawBackGround(0,0);
        drawBoxes();
        drawPlayer();
        drawButterfly();
        drawGUI();
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
        if (player == null) return;

        player.getImageView().setX(anchor_posx + player.get_x() * config.tile_size);
        for (box box : boxes) {
            box.getImageView().setX(anchor_posx + box.get_x() * config.tile_size);
        }

    }
    public void setAnchor_posy(double anchor_posy) {
        this.anchor_posy = anchor_posy;
        if (player == null) return;

        player.getImageView().setY(anchor_posy + player.get_y() * config.tile_size);
        for (box box : boxes) {
            box.getImageView().setY(anchor_posy + box.get_y() * config.tile_size);
        }

    }

    public Canvas getCanvas() {
        return canvas;
    }

    public abstract GameMap getMap();

}
