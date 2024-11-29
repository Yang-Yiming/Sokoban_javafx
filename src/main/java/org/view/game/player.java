package org.view.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.model.MapMatrix;
import org.model.config;
import org.view.level.Grass;
import org.view.level.Level;

import java.util.ArrayList;

public class player extends entity {
    //    private Image image;
//    private ImageView imageView;
    private Stage primaryStage;
    public static boolean is_moving = false;
    private int orientation = 4;

    public player(int x, int y, Stage primaryStage) {
        super(x, y, 2);
        this.primaryStage = primaryStage;
        image = new Image(getClass().getResourceAsStream("/images/player_cat/cat_stand.gif"), config.tile_size, config.tile_size, false, false);
        imageView = new ImageView(image);
        //禁用平滑属性
        imageView.setPreserveRatio(false);
        imageView.setSmooth(false);
        imageView.setFitHeight(config.tile_size);
        imageView.setFitWidth(config.tile_size);
    }

    public boolean push(entity obj, MapMatrix map) {
        if (obj.can_move(map, this.velocity_x, this.velocity_y)) {
            obj.velocity_x = this.velocity_x;
            obj.velocity_y = this.velocity_y;
            return true; // 成功移动
        }
        return false;
    }

    private Timeline cameraTimeline = null;
    public void stopCameraTimeline(){
        if(cameraTimeline != null) cameraTimeline.stop();
    }
    private void updateAnchorPos(Level level){

        if(cameraTimeline != null) cameraTimeline.stop();
        // 得到画面中心的坐标
        double width = primaryStage.getWidth();
        double height = primaryStage.getHeight();
        double midx0 = width / 2 - config.tile_size / 2;
        double midy0 = height / 2 - config.tile_size / 2;
        // 得到人物中心的坐标
        double playerx0 = imageView.getX() + config.tile_size / 2;
        double playery0 = imageView.getY() + config.tile_size / 2;
        double dx0 = midx0 - playerx0, dy0 = midy0 - playery0;
        if(dx0 < -config.tile_size * 2) dx0 += config.tile_size * 2;
        else if(dx0 > config.tile_size * 2) dx0 -= config.tile_size * 2;
        else dx0 = 0;
        if(dy0 < -config.tile_size * 2) dy0 += config.tile_size * 2;
        else if(dy0 > config.tile_size * 2) dy0 -= config.tile_size * 2;
        else dy0 = 0;
        if(Math.abs(dx0) < 10 && Math.abs(dy0) < 10) return;
        // 移动画面，使人物在中间
        level.setAnchor_posx(level.getanchor_posx() + dx0 / 30);
        level.setAnchor_posy(level.getanchor_posy() + dy0 / 30);
        level.drawMap();
        level.updateAllRadiatingEffect();

        cameraTimeline = new Timeline(new KeyFrame(Duration.seconds(0.03), e -> {
            // 得到画面中心的坐标

            double midx = width / 2 - config.tile_size / 2;
            double midy = height / 2 - config.tile_size / 2;
            // 得到人物中心的坐标
            double playerx = imageView.getX() + config.tile_size / 2;
            double playery = imageView.getY() + config.tile_size / 2;
            double dx = midx - playerx, dy = midy - playery;
            if(dx < -config.tile_size * 2) dx += config.tile_size * 2;
            else if(dx > config.tile_size * 2) dx -= config.tile_size * 2;
            else dx = 0;
            if(dy < -config.tile_size * 2) dy += config.tile_size * 2;
            else if(dy > config.tile_size * 2) dy -= config.tile_size * 2;
            else dy = 0;
            if(Math.abs(dx) < 10 && Math.abs(dy) < 10 && cameraTimeline != null) cameraTimeline.stop();
            // 移动画面，使人物在中间
            level.setAnchor_posx(level.getanchor_posx() + dx / 30);
            level.setAnchor_posy(level.getanchor_posy() + dy / 30);
            level.drawMap();
            level.updateAllRadiatingEffect();
        }));
        cameraTimeline.setCycleCount(Timeline.INDEFINITE);
        cameraTimeline.play();
    }

    public void move(MapMatrix map) {
    if (can_move(map, velocity_x, velocity_y)) {
        // 动画
        imageView.setImage(new Image(getClass().getResourceAsStream("/images/player_cat/cat_run.gif"), config.tile_size, config.tile_size, false, false));
        imageView.setX(imageView.getX() + velocity_x * config.tile_size); // 更新
        imageView.setY(imageView.getY() + velocity_y * config.tile_size); // 更新
        is_moving = true;
        TranslateTransition transition = new TranslateTransition(Duration.millis(config.move_anim_duration), imageView);
        transition.setFromX(-velocity_x * config.tile_size);
        transition.setFromY(-velocity_y * config.tile_size); // 同box.java

        if(x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight()) map.remove(x, y, type);
        x += velocity_x;
        y += velocity_y;
        if(x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight()) map.add(x, y, type); // 向那一格第i位加入

//            transition.setToX(velocity_x * config.tile_size);
//            transition.setToY(velocity_y * config.tile_size);
        transition.setToX(0);
        transition.setToY(0);
//            System.out.println(transition.getFromX() + " " + transition.getFromX() + " " + transition.getToX() + " " + transition.getToY());
        transition.setOnFinished(event -> {
            imageView.setTranslateX(0); // 重置 translateX，因为动画已经结束
            imageView.setTranslateY(0); // 重置 translateY，因为动画已经结束
            is_moving = false;
            setImageTowards(orientation);
            imageView.setPreserveRatio(false);
            imageView.setSmooth(false);
        });
        transition.play();
    }
}


    public void move(MapMatrix map, ArrayList<box> entities, Level level) {
        updateAnchorPos(level);
        int newx = x + velocity_x;
        int newy = y + velocity_y;
        if(can_move(map, velocity_x, velocity_y)){
            this.move(map);
        } else if(map.hasBox(newx, newy)) { // 暂时先这么写
            box e = entities.get(map.getBox_matrix_id(newx, newy) - 1); // 获得那个被推的箱子
            if(push(e, map)){
                //map.setBox_matrix(e.get_x(), e.get_y(), 0);
                e.move(map);
                //map.setBox_matrix(e.get_x(), e.get_y(), e.id);
                this.move(map);
//                e.setMoving(true);
            }
        }
    }

    public void set_velocity(int x, int y) {
        velocity_x = x;
        velocity_y = y;
    }

    public Image getImage() {
        return image;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
    public int getOrientation() {
        return orientation;
    }
    public ImageView getImageView() {
        return imageView;
    }
    public void setImageTowards(int orientation) { //1 上 2 下 3 左 4 右
        this.orientation = orientation;
        if(is_moving){
            if(orientation == 1) {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/player_cat/cat_run_back.gif"), config.tile_size, config.tile_size, false, false));
                imageView.setScaleX(1);
            } else if(orientation == 2) {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/player_cat/cat_run_front.gif"), config.tile_size, config.tile_size, false, false));
                imageView.setScaleX(1);
            } else if(orientation == 3) {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/player_cat/cat_run.gif"), config.tile_size, config.tile_size, false, false));
                imageView.setScaleX(-1);
            } else if(orientation == 4) {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/player_cat/cat_run.gif"), config.tile_size, config.tile_size, false, false));
                imageView.setScaleX(1);
            }
        }else{
            if(orientation == 1) {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/player_cat/cat_stand_back.gif"), config.tile_size, config.tile_size, false, false));
                imageView.setScaleX(1);
            } else if(orientation == 2) {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/player_cat/cat_stand_front.gif"), config.tile_size, config.tile_size, false, false));
                imageView.setScaleX(1);
            } else if(orientation == 3) {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/player_cat/cat_stand.gif"), config.tile_size, config.tile_size, false, false));
                imageView.setScaleX(-1);
            } else if(orientation == 4) {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/player_cat/cat_stand.gif"), config.tile_size, config.tile_size, false, false));
                imageView.setScaleX(1);
            }
        }
    }
}
