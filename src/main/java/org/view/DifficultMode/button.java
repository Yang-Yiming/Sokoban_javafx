package org.view.DifficultMode;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;


public class button {
    static ArrayList<button> all_buttons;

    Image image;
    ImageView imageView;
    Rectangle background;
    StackPane stack;
    boolean chosen = false;

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

        stack = new StackPane(background, imageView);

        this.points = points;
        this.text = text;
        this.id = id;
        set();
        imageView.setOpacity(chosen? 1 : 0.2);

        stack.setOnMouseClicked(event -> {
            chosen = !chosen;
            imageView.setOpacity(chosen? 1 : 0.2);
            DifficultMode.difficulty += chosen? points : -points;
            DifficultMode.label.setText(DifficultMode.difficulty + "");
            set();
        });
    }

    public void set() {
        switch (id) {
            case "step1" -> {
                DifficultMode.lower_step_limit1 = chosen;
                if(chosen){
                    if(DifficultMode.lower_step_limit2) {
                        DifficultMode.difficulty -= 2;
                        DifficultMode.lower_step_limit2 = false;
                    }
                    if(DifficultMode.lower_step_limit3) {
                        DifficultMode.difficulty -= 3;
                        DifficultMode.lower_step_limit3 = false;
                    }
                }
            }
            case "step2" -> {
                DifficultMode.lower_step_limit2 = chosen;
                if(chosen){
                    if(DifficultMode.lower_step_limit1) {
                        DifficultMode.difficulty -= 1;
                        DifficultMode.lower_step_limit1 = false;
                    }
                    if(DifficultMode.lower_step_limit3) {
                        DifficultMode.difficulty -= 3;
                        DifficultMode.lower_step_limit3 = false;
                    }
                }
            }
            case "step3" -> {
                DifficultMode.lower_step_limit3 = chosen;
                if(chosen){
                    if(DifficultMode.lower_step_limit1) {
                        DifficultMode.difficulty -= 1;
                        DifficultMode.lower_step_limit1 = false;
                    }
                    if(DifficultMode.lower_step_limit2) {
                        DifficultMode.difficulty -= 2;
                        DifficultMode.lower_step_limit2 = false;
                    }
                }
            }
            case "no_items" -> DifficultMode.no_items = chosen;
            case "thunder" -> DifficultMode.thunder = chosen;
            case "mushrooms" -> DifficultMode.mushrooms = chosen;
        }
        for(button btn: all_buttons) {
            btn.setOpacity();
        }
    }

    public void setOpacity() {
        switch(id) {
            case "step1" -> imageView.setOpacity(DifficultMode.lower_step_limit1? 1 : 0.2);
            case "step2" -> imageView.setOpacity(DifficultMode.lower_step_limit2? 1 : 0.2);
            case "step3" -> imageView.setOpacity(DifficultMode.lower_step_limit3? 1 : 0.2);
            case "no_items" -> imageView.setOpacity(DifficultMode.no_items? 1 : 0.2);
            case "thunder" -> imageView.setOpacity(DifficultMode.thunder? 1 : 0.2);
            case "mushrooms" -> imageView.setOpacity(DifficultMode.mushrooms? 1 : 0.2);
        }
    }
}
