package org.view.map;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.model.config;

public class map {

    private double AnchorX;
    private double AnchorY;

    private Scene scene;
    private Pane root;

    int max_nodes_per_layer;

    public map(Pane root){
        this.root = root;
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
                int begin = 0;
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

    public void draw_map() {
        int total_width = max_nodes_per_layer * (config.Map_Node_Width + config.Map_Space_Width) - config.Map_Space_Width;

        AnchorY = 10;
        AnchorX = (double)(config.ScreenWidth - total_width) / 2;

        for(int i = 0; i < node.All_Nodes.size(); i++){
            int num = node.All_Nodes.get(i).size();
            int x_bias = (total_width - config.Map_Node_Width * num - config.Map_Space_Width * (num - 1)) / 2;
            int y_bias = i * (config.Map_Node_Height + config.Map_Space_Height);
            for(int j = 0; j < num; j++){
                int x_pos = x_bias + j * (config.Map_Node_Width + config.Map_Space_Width);
                Rectangle rect = new Rectangle(config.Map_Node_Width, config.Map_Node_Height);
                rect.setX(x_pos + AnchorX); rect.setY(y_bias + AnchorY);
                this.root.getChildren().add(rect);
            }
        }

    }

    public void start(Stage primaryStage){
        this.root = new Pane();
        scene = new Scene(root, config.ScreenWidth, config.ScreenHeight);
        primaryStage.setTitle("Sokoban Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        random_generate(5,4);
        draw_map();
    }

}
