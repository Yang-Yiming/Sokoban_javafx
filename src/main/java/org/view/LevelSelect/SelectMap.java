package org.view.LevelSelect;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import org.data.mapdata;
import org.model.User;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SelectMap {
    static double AnchorX, AnchorY;
    private Stage stage;
    private Scene scene;
    private Pane root;

    private ArrayList<MapNode> nodes;

    public SelectMap(Stage stage) {
        this.stage = stage;
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
            node.button.setText("Level " + (i+1));
            if(nodes == null) nodes = new ArrayList<>();
            nodes.add(node);
        }
    }

    public void draw() {
        root.getChildren().clear();
        scene.getStylesheets().add("file://" + new java.io.File("./src/main/resources/css/styles.css").getAbsolutePath());
        root.setLayoutX(AnchorX); root.setLayoutY(AnchorY); // 设置根节点的位置

        GridPane gridPane = new GridPane();


        for (MapNode node : nodes) {
            if(node.is_locked){
                node.button.getStyleClass().add("button-level-locked");
            } else {
                node.button.getStyleClass().add("button-level");
            }
            gridPane.add(node.button, node.index, node.y + MapNode.YRange);
        }
        root.getChildren().add(gridPane);

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
