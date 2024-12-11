package org.view.LevelSelect;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.model.User;
import org.model.config;
import org.view.level.Grass;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class SelectMap {
    static double AnchorX, AnchorY;
    private Stage stage;
    private Scene scene;
    private Pane root;
    private GridPane gridPane;

    private ArrayList<MapNode> nodes;

    public SelectMap(Stage stage) {
        this.stage = stage;
        gridPane = new GridPane();
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
        generateGridPane();
    }

    public void generateGridPane(){
        if(gridPane == null) gridPane = new GridPane();
        gridPane.getChildren().clear();

        for (MapNode node : nodes) {
            gridPane.add(node.button, node.x, node.y);
        }

        // 绘制小路
        for (int i = 0; i < nodes.size() - 1; i++) {
            MapNode node1 = nodes.get(i); MapNode node2 = nodes.get(i + 1);
            int x = node1.x, y =node1.y; int toX = node2.x, toY = node2.y;
            boolean move_down = toY > y;
            while(x!=toX && y!=toY){
                int possible_moves_type = 0;
                if(x < toX) possible_moves_type++;
                if((y < toY && move_down) || (y > toY && !move_down)) possible_moves_type++;
                boolean move_x = Math.random() * possible_moves_type < 1;
                if(move_x) x++;
                else y += move_down ? 1 : -1;

                Rectangle rect = new Rectangle(config.Map_Node_Width,config.Map_Node_Width);
                rect.setFill(Color.BROWN);
                gridPane.add(rect, x, y);
            }
        }

        gridPane.setHgap(0); gridPane.setVgap(0);
        
    }

    public void draw() {
        root.getChildren().clear(); root.setStyle("-fx-background-color: #FFFFFF");
        scene.getStylesheets().add("file://" + new java.io.File("./src/main/resources/css/styles.css").getAbsolutePath());
        root.setLayoutX(AnchorX); root.setLayoutY(AnchorY); // 设置根节点的位置

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
