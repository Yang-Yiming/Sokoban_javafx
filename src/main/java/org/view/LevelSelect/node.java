package org.view.LevelSelect;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import org.model.config;

import java.util.ArrayList;

public class node {

    public static ArrayList<ArrayList<node>> All_Nodes = new ArrayList<>();

    public static void connect(node node1, node node2) {
        if(node2.layer <= node1.layer || node2.layer - node1.layer > 1) {
            return;
        }
        node1.NextLayerConnectedNodes.add(node2);
        node2.LastLayerConnectedNodes.add(node1);
        node1.is_connected = true; node2.is_connected = true;
    }

    private final int layer;
    private int index;
    private int type;
    private boolean is_connected;

    private Scene scene;
    private Button button;

    private ArrayList<node> NextLayerConnectedNodes = new ArrayList<node>();
    private ArrayList<node> LastLayerConnectedNodes = new ArrayList<node>();


    public node(int layer, int index, int type) {
        this.layer = layer;
        this.index = index;
        this.type = type;
        this.is_connected = false;

        this.button = new Button();
        button.setPrefSize(config.Map_Node_Width, config.Map_Node_Height);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GRAY);
        dropShadow.setRadius(5); dropShadow.setOffsetX(2); dropShadow.setOffsetY(2);
        this.button.setEffect(dropShadow); // 添加阴影效果

        if(layer >= All_Nodes.size()){
            All_Nodes.add(new ArrayList<>());
        }

        All_Nodes.get(layer).add(this);
    }

    //getter setter
    public int getLayer() {
        return layer;
    }

    public int getIndex() {
        return index;
    }

    public int getType() {
        return type;
    }

    public Button getButton() {
        return button;
    }

    public boolean is_connected() {return is_connected;}

    public ArrayList<node> getNextLayerConnectedNodes() {
        return NextLayerConnectedNodes;
    }

    public ArrayList<node> getLastLayerConnectedNodes() {
        return LastLayerConnectedNodes;
    }


    public double get_posX() {
        return button.getLayoutX() + (double) config.Map_Node_Width / 2;
    }
    public double get_left_posX(){
        return button.getLayoutX();
    }
    public double get_right_posX(){
        return button.getLayoutX() + (double)config.Map_Node_Width;
    }
    public double get_posY() {
        return button.getLayoutY() + (double) config.Map_Node_Height / 2;
    }
    public double get_up_posY(){
        return button.getLayoutY();
    }
    public double get_down_posY(){
        return button.getLayoutY() + (double)config.Map_Node_Height;
    }

}