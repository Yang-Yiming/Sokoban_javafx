package org.view.LevelSelect;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.model.SavingManager;
import org.model.User;
import org.model.config;
import org.view.level.LevelManager;

import java.io.FileNotFoundException;
import java.sql.Time;


public class Chest {
    Image hint = new Image(getClass().getResourceAsStream("/images/hint.png"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image plus = new Image(getClass().getResourceAsStream("/images/plus.png"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image withdraw = new Image(getClass().getResourceAsStream("/images/withdraw.png"), config.Map_Node_Width, config.Map_Node_Width, false, false);

    Image chest_open = new Image(getClass().getResourceAsStream("/images/treasure_opened.png"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image chest_closed = new Image(getClass().getResourceAsStream("/images/treasure_closed.png"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image chest_open_anim = new Image(getClass().getResourceAsStream("/images/treasure_open.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Timeline open_counter;

    ImageView imageView = new ImageView(chest_closed);
    Pane root;

    int x, y;
    boolean opened = false;

    int[] contain = new int[3]; // hint/ plus/ withdraw
    User user;
    public Chest(int x, int y, Pane root, User user) {
        this.x = x;
        this.y = y;
        this.root = root;
        this.user = user;
        open_counter = new Timeline(
                new KeyFrame(Duration.millis(400), e-> imageView.setImage(chest_open_anim)),
                new KeyFrame(Duration.millis(500), e-> imageView.setImage(chest_open)));
        open_counter.setCycleCount(1);
        contain = new int[]{1, 1, 1};
    }

    public void open(Pane root) {
        if(opened) return;
        open_counter.play();
        open_counter.setOnFinished(e->{
            handle_open(root);
            opened = true;
            config.item_hintNumber += contain[0];
            config.item_plusNumber += contain[1];
            config.item_withdrawNumber += contain[2];
            LevelManager.item_hintText.setText("x" + config.item_hintNumber);
            LevelManager.item_plusText.setText("x" + config.item_plusNumber);
            LevelManager.item_withdrawText.setText("x" + config.item_withdrawNumber);

            if(config.item_hintNumber == 1) root.getChildren().add(LevelManager.item_hint);
            if(config.item_plusNumber == 1) root.getChildren().add(LevelManager.item_plus);
            if(config.item_withdrawNumber == 1) root.getChildren().add(LevelManager.item_withdraw);

            //写进存档
            user.setItem_hintNumber(config.item_hintNumber);
            user.setItem_plusNumber(config.item_plusNumber);
            user.setItem_withdrawNumber(config.item_withdrawNumber);
            try{
                SavingManager.save();
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }

        });
    }

    private void handle_open(Pane root) {
        HBox itemHbox = new HBox();
        root.getChildren().add(itemHbox);
        if(contain[0] > 0) {
            ImageView hintView = new ImageView(hint);
            itemHbox.getChildren().add(hintView);
        }
        if(contain[1] > 0) {
            ImageView plusView = new ImageView(plus);
            itemHbox.getChildren().add(plusView);
        }
        if(contain[2] > 0) {
            ImageView withdrawView = new ImageView(withdraw);
            itemHbox.getChildren().add(withdrawView);
        }
        itemHbox.setSpacing(10);
        itemHbox.layout();
        itemHbox.setLayoutX(SelectMap.AnchorX + x * config.Map_Node_Width + 20);
        itemHbox.setLayoutY(SelectMap.AnchorY + y * config.Map_Node_Width + 50);
        itemHbox.setScaleX(0.1); itemHbox.setScaleY(0.1);
        itemHbox.setOpacity(1);

        Timeline timeline = new Timeline(); int[] time_counter = new int[]{80};
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(10), e-> {
                    if(!root.getChildren().contains(itemHbox)) {
                        root.getChildren().add(itemHbox);
                    }
                    if(itemHbox.getScaleX() < 1) {
                        itemHbox.setScaleX(itemHbox.getScaleX() + 0.1);
                        itemHbox.setScaleY(itemHbox.getScaleY() + 0.1);
                    }
                    if(itemHbox.getScaleX() >= 1) {
                        time_counter[0]--;
                    }
                    if(time_counter[0] <= 0) {
                        itemHbox.setOpacity(itemHbox.getOpacity() - 0.1);
                    }
                    if(itemHbox.getOpacity() <= 0.1) {
                        root.getChildren().remove(itemHbox);
                        timeline.stop();
                    }
                })
        );
        timeline.setCycleCount(-1);
        timeline.play();
    }
}
