package org.view.DifficultMode;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.view.level.LevelManager;

import java.util.ArrayList;


public class button {
    Font pixelFont = Font.loadFont(getClass().getResource("/font/pixel.ttf").toExternalForm(), 12);

    static ArrayList<button> all_buttons;

    Image image;
    ImageView imageView;
    Rectangle background;
    StackPane stack;
    public boolean chosen = false;

    int points;
    String text;
    String id;

    public button(String image1_path, int points, String text, String id) {
        if(all_buttons == null) all_buttons = new ArrayList<>();
        all_buttons.add(this);

        this.image = new Image(getClass().getResourceAsStream(image1_path));
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(75); this.imageView.setFitHeight(75);
        this.background = new Rectangle(0, 0, 75, 75);
        background.setFill(javafx.scene.paint.Color.rgb(4, 4, 4));
        background.setOpacity(0.7);
        background.setArcWidth(10);
        background.setArcHeight(10);
        background.setStroke(javafx.scene.paint.Color.rgb(255, 255, 255));
        Label label = new Label(text);
        label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-border-color: white; -fx-border-width: 2px;");
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setFont(pixelFont);

        stack = new StackPane(background, imageView);

        this.points = points;
        this.text = text;
        this.id = id;
        set();
        imageView.setOpacity(chosen? 1 : 0.2);

        stack.setOnMouseClicked(event -> {
            chosen = !chosen;
            setOpacity();
            set();
            DifficultMode.difficulty = 0;
            for(button btn: all_buttons) {
                if(btn.chosen) DifficultMode.difficulty += btn.points;
            }
            DifficultMode.label.setText(DifficultMode.difficulty + "");
        });

        stack.setOnMouseEntered(event -> {
            stack.getChildren().add(label);
        });
        stack.setOnMouseExited(event -> {
            stack.getChildren().remove(label);
        });
    }

    public void set() {
        switch (id) {
            case "step1" -> {
                if(chosen){
                    DifficultMode.lower_step_limit2.chosen = false;
                    DifficultMode.lower_step_limit3.chosen = false;
                }
            }
            case "step2" -> {
                if(chosen){
                    DifficultMode.lower_step_limit1.chosen = false;
                    DifficultMode.lower_step_limit3.chosen = false;
                }
            }
            case "step3" -> {
                if(chosen){
                    DifficultMode.lower_step_limit1.chosen = false;
                    DifficultMode.lower_step_limit2.chosen = false;
                }
            }
        }
        if(LevelManager.item_plus != null && DifficultMode.no_items != null) LevelManager.item_plus.setOpacity(DifficultMode.no_items.chosen? 0.3 : 1);
        if(LevelManager.item_hint != null && DifficultMode.no_items != null) LevelManager.item_hint.setOpacity(DifficultMode.no_items.chosen? 0.3 : 1);
        if(LevelManager.item_withdraw != null && DifficultMode.no_items != null) LevelManager.item_withdraw.setOpacity(DifficultMode.no_items.chosen? 0.3 : 1);
        for(button btn: all_buttons) {
            btn.setOpacity();
        }
    }


    public void setOpacity() {
        imageView.setOpacity(chosen? 1 : 0.2);
    }
}
