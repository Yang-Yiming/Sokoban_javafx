package org.view.DifficultMode;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;


public class button {
    Image image;
    ImageView imageView;
    Rectangle background;
    StackPane stack;
    boolean chosen = false;

    int points;
    String text;
    String id;

    public button(String image1_path, int points, String text, String id) {
        this.image = new Image(getClass().getResourceAsStream(image1_path));
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(75); this.imageView.setFitHeight(75);
        this.background = new Rectangle(0, 0, 75, 75);
        background.setFill(javafx.scene.paint.Color.rgb(112, 100, 100));
        background.setOpacity(0.7);
        background.setArcWidth(10);
        background.setArcHeight(10);
        background.setStroke(javafx.scene.paint.Color.rgb(112, 90, 1));

        stack = new StackPane(background, imageView);

        this.points = points;
        this.text = text;
        this.id = id;
        set(); imageView.setOpacity(chosen? 1 : 0.6);

        stack.setOnMouseClicked(event -> {
            chosen = !chosen;
            imageView.setOpacity(chosen? 1 : 0.6);
            DifficultMode.difficulty += chosen? points : -points;
            DifficultMode.label.setText(DifficultMode.difficulty + "");
            set();
        });
    }

    public void set() {
        if(id.equals("step1"))
            DifficultMode.lower_step_limit1 = chosen;
        else if(id.equals("step2"))
            DifficultMode.lower_step_limit2 = chosen;
        else if(id.equals("step3"))
            DifficultMode.lower_step_limit3 = chosen;
        else if(id.equals("no_items"))
            DifficultMode.no_items = chosen;
        else if(id.equals("thunder"))
            DifficultMode.thunder = chosen;
        else if(id.equals("mushrooms"))
            DifficultMode.mushrooms = chosen;
        //e.g.: DifficultMode.thunder = chosen;
    }
}
