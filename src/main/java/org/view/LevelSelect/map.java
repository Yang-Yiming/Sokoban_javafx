package org.view.LevelSelect;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import org.model.config;

import java.awt.*;

public class map {

    private double AnchorX;
    private double AnchorY;

    private Scene scene;
    private Pane root;

    int max_nodes_per_layer = 1;

    public map(Pane root){
        this.root = root;
        this.scene = new Scene(root, config.ScreenWidth, config.ScreenHeight);
    }

    public void random_generate(int layer_num, int max_nodes_per_layer) {
        this.max_nodes_per_layer = max_nodes_per_layer;
        for(int i = 0; i< layer_num; i++) {
            int node_num = config.randint(1,max_nodes_per_layer);
            for(int j = 0; j < node_num; j++) {
                node node = new node(i, j, 0);
            }
        }

        for(int i = 0; i < layer_num - 1; i++){
            for(int j = 0; j < node.All_Nodes.get(i).size(); j++){
                node one = node.All_Nodes.get(i).get(j);
                int connect_num = config.randint(1,node.All_Nodes.get(i).size() - j - 1);
                int begin;
                for(begin = 0; begin < node.All_Nodes.get(i+1).size(); begin++){
                    if(!node.All_Nodes.get(i+1).get(begin).is_connected()){ break; }
                }
                for(int k = begin; k < connect_num; k++){
                    node two = node.All_Nodes.get(i+1).get(k);
                    node.connect(one, two);
                }

            }
        }
    }

    public void generate_cubic_curve(double startX, double startY, double endX, double endY) {
        // 计算控制点
        double controlX1 = (startX + endX) / 2 - (endY - startY) / 2;
        double controlY1 = (startY + endY) / 2 - (endX - startX) / 2;
        double controlX2 = (startX + endX) / 2 + (endY - startY) / 2;
        double controlY2 = (startY + endY) / 2 + (endX - startX) / 2;

        // 创建路径
        Path path = new Path();

        // 设置起点
        MoveTo moveTo = new MoveTo(startX, startY);
        path.getElements().add(moveTo);

        // 产生曲线
        CubicCurveTo cubicCurveTo = new CubicCurveTo(controlX1, controlY1, controlX2, controlY2, endX, endY);
        path.getElements().add(cubicCurveTo);

        // 设置曲线的形态
        path.setStroke(Color.BLUE); // 颜色
        path.setStrokeWidth(2); // 宽度
        path.setStrokeLineCap(StrokeLineCap.ROUND); //端点

        root.getChildren().add(path); // 加入曲线
    }

    public void draw_map() {
        int total_width = max_nodes_per_layer * config.Map_Node_Width + (max_nodes_per_layer - 1) * config.Map_HGap;
        AnchorX = (double) (config.ScreenWidth - total_width) / 2;
        AnchorY = 20;

        scene.getStylesheets().add("file://" + new java.io.File("./src/main/resources/css/styles.css").getAbsolutePath());
        root.setLayoutX(AnchorX); root.setLayoutY(AnchorY);

        VBox vbox = new VBox(config.Map_VGap);
        vbox.setAlignment(Pos.CENTER);

        for(int i = 0; i < node.All_Nodes.size(); i++){
            HBox hbox = new HBox(config.Map_HGap);
            hbox.setAlignment(Pos.CENTER);
            for(int j = 0; j < node.All_Nodes.get(i).size(); j++){
                node one = node.All_Nodes.get(i).get(j);
                // 设置按钮
                Button button = one.getButton();
                button.setText(one.getLayer() + "-" + one.getIndex());
                button.getStyleClass().add("button-level");
                button.setOnAction(event -> {
                    System.out.println("Layer: " + one.getLayer() + " Index: " + one.getIndex());
                });
                hbox.getChildren().add(button);
                // 设置连接线
                if(i < node.All_Nodes.size() - 1  && one.is_connected()){
                    for(node two : one.getNextLayerConnectedNodes()){
                        generate_cubic_curve(one.get_posX(), one.get_down_posY(), two.get_posX(), two.get_up_posY());
                    }
                }
            }
            vbox.getChildren().add(hbox);
        }
        root.getChildren().add(vbox);
    }

    public Scene getScene() {
        return scene;
    }

}
