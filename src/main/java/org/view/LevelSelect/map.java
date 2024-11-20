package org.view.LevelSelect;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.paint.Color;
import org.model.config;

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

    private static void random_connect(int layer_num) {
        for(int i = 0; i < layer_num - 2; i++){
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

    public void random_generate_map(int layer_num, int max_nodes_per_layer) {
        this.max_nodes_per_layer = max_nodes_per_layer;
        for(int i = 0; i< layer_num - 1; i++) {
            int node_num = config.randint(1,max_nodes_per_layer);
            for(int j = 0; j < node_num; j++) {
                new node(i, j, 0);
            }
        }
        new node(layer_num - 1, 0, 0);
        random_connect(layer_num);
    }

    public void linear_generate_map(int layer_num) {
        for(int i = 0; i< layer_num; i++) {
            new node(i, 0, 0);
            if(i > 0){
                node.connect(node.All_Nodes.get(i-1).get(0), node.All_Nodes.get(i).get(0));
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

    public void draw_nodes(boolean is_vertical) {
        int total_width = max_nodes_per_layer * config.Map_Node_Width + (max_nodes_per_layer - 1) * config.Map_Node_Gap;
        int total_height = max_nodes_per_layer * config.Map_Node_Height + (max_nodes_per_layer - 1) * config.Map_Layer_Gap;
        AnchorX = (double) (config.ScreenWidth - total_width) / 2;
        AnchorY = (double) (config.ScreenHeight - total_height) / 2;

        scene.getStylesheets().add("file://" + new java.io.File("./src/main/resources/css/styles.css").getAbsolutePath());
        root.setLayoutX(AnchorX); root.setLayoutY(AnchorY);

        VBox super_vbox = new VBox(config.Map_Layer_Gap);
        super_vbox.setAlignment(Pos.CENTER); // vertical用
        HBox super_hbox = new HBox(config.Map_Layer_Gap);
        super_hbox.setAlignment(Pos.CENTER); // horizontal用

        for(int i = 0; i < node.All_Nodes.size(); i++){
            HBox hbox = new HBox(config.Map_Node_Gap);
            hbox.setAlignment(Pos.CENTER); // 这两个vertical用
            VBox vbox = new VBox(config.Map_Node_Gap);
            vbox.setAlignment(Pos.CENTER); // 这两个horizontal用

            for(int j = 0; j < node.All_Nodes.get(i).size(); j++){
                node one = node.All_Nodes.get(i).get(j);
                // 设置按钮
                Button button = one.getButton();
                button.setText(one.getLayer() + "-" + one.getIndex());
                button.getStyleClass().add("button-level");
                button.setOnAction(event -> {
                    System.out.println("Layer: " + one.getLayer() + " Index: " + one.getIndex());
                });
                if(is_vertical)
                    hbox.getChildren().add(button);
                else
                    vbox.getChildren().add(button);
            }
            if(is_vertical)
                super_vbox.getChildren().add(hbox);
            else
                super_hbox.getChildren().add(vbox);
        }
        if(is_vertical)
            root.getChildren().add(super_vbox);
        else
            root.getChildren().add(super_hbox);
    }
    public void draw_line(boolean is_vertical) {
        for(int i = 0; i < node.All_Nodes.size() - 1; i++){
            for(int j = 0; j < node.All_Nodes.get(i).size(); j++){
                node one = node.All_Nodes.get(i).get(j);
                for(node two: one.getNextLayerConnectedNodes()){
                    if(is_vertical)
                        generate_cubic_curve(one.get_posX(), one.get_down_posY(), two.get_posX(), two.get_up_posY());
                    else
                        generate_cubic_curve(one.get_right_posX(), one.get_posY(), two.get_left_posX(), two.get_posY());
                }
            }
        }
    }

    public void draw_map(boolean is_vertical) {
        draw_nodes(is_vertical);
        draw_line(is_vertical);
    }

    public Scene getScene() {
        return scene;
    }

}
