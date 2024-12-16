package org.view.LevelSelect;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.model.User;
import org.model.config;

public class Cat {
    Image cat_stand = new Image(getClass().getResourceAsStream("/images/player_cat/cat_stand.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image cat_run = new Image(getClass().getResourceAsStream("/images/player_cat/cat_run.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image cat_run_front = new Image(getClass().getResourceAsStream("/images/player_cat/cat_run_front.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image cat_run_back = new Image(getClass().getResourceAsStream("/images/player_cat/cat_run_back.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image cat_stand_front = new Image(getClass().getResourceAsStream("/images/player_cat/cat_stand_front.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image cat_stand_back = new Image(getClass().getResourceAsStream("/images/player_cat/cat_stand_back.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    ImageView imageView;
    User user;
    public int x, y;
    public Cat(User user) {
        imageView = new ImageView(cat_stand);
        imageView.setFitHeight(config.Map_Node_Width);
        imageView.setFitWidth(config.Map_Node_Width);
        this.user = user;
        if(user.getLevelAt() % 5 == 0){
            if(user.getLevelAt() == 0) {x = 0; y = 0;}
            else {x = 12; y = 0;}
        }else {x = ((user.getLevelAt() % 5) - 1) * 3; y = 0;}
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


