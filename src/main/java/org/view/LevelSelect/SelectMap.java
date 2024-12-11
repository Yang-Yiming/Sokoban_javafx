package org.view.LevelSelect;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.model.User;
import org.model.config;
import org.view.level.Grass;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

class Cat {
    Image cat_stand = new Image(getClass().getResourceAsStream("/images/player_cat/cat_stand.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    Image cat_run = new Image(getClass().getResourceAsStream("/images/player_cat/cat_run.gif"), config.Map_Node_Width, config.Map_Node_Width, false, false);
    ImageView imageView;
    int x, y;
    public Cat() {
        imageView = new ImageView(cat_stand);
        imageView.setFitHeight(config.Map_Node_Width);
        imageView.setFitWidth(config.Map_Node_Width);
        x = 0; y = 0;
    }

    public void move(int dir){ // 0 : right 1:down 2:up
        imageView = new ImageView(cat_run);
        imageView.setFitHeight(config.Map_Node_Width); imageView.setFitWidth(config.Map_Node_Width);
        TranslateTransition tt = new TranslateTransition(Duration.millis(config.move_anim_duration), imageView);
        if(dir == 0) {
            tt.setByX(config.Map_Node_Width); x++;
        } else if (dir == 1){
            tt.setByY(config.Map_Node_Width); y++;
        } else {
            tt.setByY(-config.Map_Node_Width); y--;
        }
        tt.play();
    }
}


public class SelectMap {
    static double AnchorX, AnchorY;
    private Stage stage;
    private Scene scene;
    private Pane root;
    private Cat cat;

    private ArrayList<MapNode> nodes;

    public SelectMap(Stage stage) {
        this.stage = stage;
        cat = new Cat();
        if(stage.getScene() != null){
            this.scene = stage.getScene();
            this.root = (Pane) scene.getRoot();
        } else {
            this.root = new Pane();
            this.scene = new Scene(root);
        }
    }

    public void add_levels(int[][][] maps, User user){
        MapNode.maps = maps;
        for(int i = 0; i < maps.length; i++){
            MapNode node = new MapNode(i, stage);
            node.target_level = i;
            if(user.getLevelAt() < i) node.is_locked = true;
            if(nodes == null) nodes = new ArrayList<>();
            nodes.add(node);
        }
    }

    public void draw() {
        root.getChildren().clear(); root.setStyle("-fx-background-color: #FFFFFF");
        scene.getStylesheets().add("file://" + new java.io.File("./src/main/resources/css/styles.css").getAbsolutePath());
        root.setLayoutX(0); root.setLayoutY(0); // 设置根节点的位置

        // 画goals
        for(MapNode node: nodes) {
            node.button.setLayoutX(AnchorX + node.x * config.Map_Node_Width);
            node.button.setLayoutY(AnchorY + node.y * config.Map_Node_Width);
            root.getChildren().add(node.button);
        }
        // 画猫
        cat.imageView.setX(AnchorX + cat.x * config.Map_Node_Width);
        cat.imageView.setY(AnchorY + cat.y * config.Map_Node_Width);
        root.getChildren().add(cat.imageView);
    }

    public void Move() {
        // 根据鼠标拖动改变anchor_posx anchor_posy
        // 鼠标是否在拖动根据鼠标拖动改变anchor_posx
        AtomicReference<Double> del_posx = new AtomicReference<>((double) 0);
        AtomicReference<Double> del_posy = new AtomicReference<>((double) 0);

        // 添加鼠标按下事件监听器
        scene.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                del_posx.set(AnchorX - event.getSceneX());
                del_posy.set(AnchorY - event.getSceneY());
            }
        });
        scene.setOnMouseDragged(event -> {
            AnchorX = event.getSceneX() + del_posx.get();
            AnchorY = event.getSceneY() + del_posy.get();
            draw();
        });
    }

    public Scene getScene() {
        return scene;
    }
}
