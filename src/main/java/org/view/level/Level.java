package org.view.level;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.model.*;
import org.model.Solve.Solve;
import org.view.game.box;
import org.view.game.player;
import org.view.VisualEffects.GlowRectangle;
import org.view.menu.Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public abstract class Level {

    protected GameMap map;
    
    public Pane root;
    org.view.game.player player;
    ArrayList<box> boxes;
    protected Stage primaryStage;

    protected Solve solve; // 解决关卡
    protected Canvas canvas; // 用来放grass

//    private GUIController guiController; // 显示gui
    private Pane gui_root; // gui的root
    protected User user; // 史山

    // 从此处开始绘制 // 这可真是依托史山啊
    protected double anchor_posx;
    protected double anchor_posy;

    // 在大地图上的位置
    protected int sublevel_begin_x = 0, sublevel_begin_y = 0; // 在大地图的位置

    protected Rectangle fadeRectangle;
    protected Timeline fadeTimeline;
    protected Grass grass = new Grass();
    public boolean isEnd = false;
    public void init() {
        isEnd = false;
        //用 a* 跑出步数限制 先 +5
        if(stepLimit == 0) stepLimit = solve_moves().length() + 5;
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
                    player = new player(x, y, primaryStage, false);
                    player.getImageView().setX(anchor_posx + x * config.tile_size);
                    player.getImageView().setY(anchor_posy + y * config.tile_size);
                    // System.out.println("x : " + x + ", y : " + y);
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
    public void createFadeTimeline(){
        //让长方形逐渐变淡
        if(fadeTimeline != null) fadeTimeline.stop();
        fadeTimeline = new Timeline(new KeyFrame(Duration.seconds(0.02), e -> {
            fadeRectangle.setOpacity(fadeRectangle.getOpacity() - 0.02);
            if(fadeRectangle.getOpacity() <= 0){
                fadeRectangle.setOpacity(0);
                fadeTimeline.stop();
                root.getChildren().remove(fadeRectangle);
            }
        }));
        fadeTimeline.setCycleCount(Animation.INDEFINITE);
        fadeTimeline.play();
        config.timelines.add(fadeTimeline);
    }

    public void generate_glow_rects() {
        GlowRectangle.glowRectangles.clear();
        for (int y = sublevel_begin_y; y < sublevel_begin_y + map.getHeight(); ++y) {
            for (int x = sublevel_begin_x; x < sublevel_begin_x + map.getWidth(); ++x) {
                if (map.hasGoal(x, y)) {
                    new GlowRectangle(x, y);
                }
            }
        }
    }

    protected Timeline butterflyTimeline = null;
    public void createButterflyTimeline(){
//        if(butterflyTimeline == null)
            butterflyTimeline = new Timeline(new KeyFrame(Duration.seconds(0.08), e -> {
                grass.updateTimeid();
                drawMap();
            }));
        butterflyTimeline.setCycleCount(Animation.INDEFINITE);
        butterflyTimeline.play();
        config.timelines.add(butterflyTimeline);
    }
    public void stopTimelines(){
//        butterflyTimeline.stop();
//        this.player.stopCameraTimeline();
//        GlowRectangle.timeline.stop();
        for(Timeline timeline : config.timelines){
            if(timeline != null) timeline.stop();
        }
        //GlowRectangle.timeline = null;
    }

    public Level(Pane root, Stage primaryStage, User user) {
        this.root = root;
        this.primaryStage = primaryStage;
        if(canvas == null)
            canvas = new Canvas(primaryStage.getWidth(), primaryStage.getHeight());
        this.user = user;
        //load_gui(user);
    }
    Font pixelFont = Font.loadFont(getClass().getResource("/font/pixel.ttf").toExternalForm(), 20);
    Text stepText, stepLimitText;
    private int step, stepLimit = 0;
    public int getStep(){
        return step;
    }
    public int getStepLimit(){
        return stepLimit;
    }
    public void setStepLimit(int stepLimit){
        this.stepLimit = stepLimit;
        stepLimitText.setText("步数限制: " + stepLimit);
    }
    public void addStep(){
        ++step;
        stepText.setText("移动步数: " + step);
    }
    protected void load_gui(User user) {
        step = 0;
        stepText = new Text("移动步数: " + step);
        stepText.setX(40);
        stepText.setY(60);
        //棕色
        stepText.setFill(Color.web("#55371d"));
        stepText.setFont(pixelFont);
        root.getChildren().add(stepText);

        stepLimitText = new Text("步数限制: " + stepLimit);
        stepLimitText.setX(40);
        stepLimitText.setY(100);
        //棕色
        stepLimitText.setFill(Color.web("#55371d"));
        stepLimitText.setFont(pixelFont);
        root.getChildren().add(stepLimitText);

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
                grass.addGrass(canvas, dx, dy, anchor_posx, anchor_posy, config.tile_size);
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
                grass.addButterfly(root, dx, dy, anchor_posx, anchor_posy, config.tile_size);
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
                grass.addButterflyShadow(root, dx, dy, anchor_posx, anchor_posy, config.tile_size);
    }

    public void drawBackGround() {
        int tileSize = config.tile_size;
        for (int y = 0; y < map.getHeight(); ++y) {
            for (int x = 0; x < map.getWidth(); ++x) {
                double posx = anchor_posx + x * tileSize;
                double posy = anchor_posy + y * tileSize;

                if (map.hasGoal(x, y)) {
                    ImageView goal = new ImageView(new Image(getClass().getResourceAsStream("/images/goal.png"), config.tile_size, config.tile_size, false, false));
                    goal.setFitWidth(tileSize);
                    goal.setFitHeight(tileSize);
                    goal.setX(posx);
                    goal.setY(posy);
                    root.getChildren().add(goal);
                }
            }
        }
        //将所有 glowrectangles 里的成员置于图层最上方
        for(GlowRectangle glowRectangle : GlowRectangle.glowRectangles){
            root.getChildren().add(glowRectangle.getRect());
        }
    }

    private boolean inScreen(double posx, double posy) {
        double width = primaryStage.getWidth();
        double height = primaryStage.getHeight();
        return !(posx < -config.tile_size) &&
                !(posy < -config.tile_size) &&
                !(posx > width + config.tile_size) &&
                !(posy > height + config.tile_size);
    }

    public void drawBoxesAndWall() {
        int tileSize = config.tile_size;
        for (int y = 0; y < map.getHeight(); ++y) {
            for (int x = 0; x < map.getWidth(); ++x) {
                double posx = anchor_posx + x * tileSize;
                double posy = anchor_posy + y * tileSize;

                if (map.hasWall(x, y) && inScreen(posx, posy)) {
                    Image wallImage = map.getWallImage(x, y);
                    ImageView wall = new ImageView(wallImage);
                    wall.setFitWidth(tileSize);
                    wall.setFitHeight(tileSize * (1.0 + config.wall_angle_amount));
                    wall.setX(posx);
                    wall.setY(posy - tileSize * config.wall_angle_amount);
                    root.getChildren().add(wall);
                } else if (map.hasBox(x, y) && inScreen(posx, posy)) { // 暂时先这么写
                     box e = boxes.get(map.getBox_matrix_id(x, y) - 1);
                     if(!root.getChildren().contains(e.getImageView()))
                        root.getChildren().add(e.getImageView());
                }
            }
        }
    }
    public void updateAllRadiatingEffect() {
        for (GlowRectangle glowRectangle: GlowRectangle.glowRectangles) {
            if(map.hasGoal(glowRectangle.getX() + sublevel_begin_x, glowRectangle.getY() + sublevel_begin_y))
                glowRectangle.update(glowRectangle.getX(), glowRectangle.getY());
        }
    }

    public void drawBoxes() {
        for(box box : boxes) {
            root.getChildren().add(box.getImageView());
        }
    }
    public void drawPlayer() {
        root.getChildren().add(player.getImageView());
    }
//    Button settingsButton;
    public void drawGUI() {
        root.getChildren().add(stepText);
        root.getChildren().add(stepLimitText);
//        if(settingsButton == null) {
//            Settings settings = new Settings();
//            settingsButton = settings.createButton(root);
//            settingsButton.setLayoutX(root.getPrefWidth() - 80);
//        }
//        root.getChildren().add(settingsButton);
//        if(user == null) return;
//        guiController.update(user.getMoveCount());
//        root.getChildren().addAll(gui_root);
    }


    public void drawMap() {
        root.getChildren().clear(); // 先清空一下地图
        drawGrass();
        drawButterflyShadow();
        drawBackGround();
//        drawBoxes();
        drawPlayer();
        drawBoxesAndWall();
        drawButterfly();
        drawGUI();
        root.getChildren().add(fadeRectangle);
    }

    public boolean isWin() {
        for (int y = 0; y < map.getHeight(); ++y)
            for (int x = 0; x < map.getWidth(); ++x)
                if (map.hasGoal(x, y) && !map.hasBox(x, y))
                    return false;
        return true;
    }

    public String solve_moves() {
        solve = new Solve(map);
        if(!config.auto_check_fail && !config.this_is_hint)
            return solve.simple_search()? "N":" ";

        return solve.aStarSearch();
    }
    public char solve_next_move() {
        String solution = solve_moves();
        if(solution.isEmpty()) return 'E'; // 赢了
        return solution.charAt(0);
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
        GlowRectangle.anchor_posx = anchor_posx;
        if (player == null) return;

        player.getImageView().setX(anchor_posx + player.get_x() * config.tile_size);
        for (box box : boxes) {
            box.getImageView().setX(anchor_posx + box.get_x() * config.tile_size);
        }

    }
    public void setAnchor_posy(double anchor_posy) {
        this.anchor_posy = anchor_posy;
        GlowRectangle.anchor_posy = anchor_posy;
        if (player == null) return;

        player.getImageView().setY(anchor_posy + player.get_y() * config.tile_size);
        for (box box : boxes) {
            box.getImageView().setY(anchor_posy + (box.get_y() - config.box_angle_amount) * config.tile_size);
        }

    }

    public Canvas getCanvas() {
        return canvas;
    }

    public abstract GameMap getMap();
    public Pane getRoot() {
        return root;
    }
    public player getPlayer() {
        return player;
    }
}
