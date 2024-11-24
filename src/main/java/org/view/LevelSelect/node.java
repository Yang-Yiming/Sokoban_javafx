package org.view.LevelSelect;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.model.config;

import org.view.level.LevelManager;

import java.util.ArrayList;

public class node {

    public static ArrayList<ArrayList<node>> All_Nodes = new ArrayList<>();
    public static LevelManager levelManager; // 也不知道导出是怎么个事（ 但无所谓（）

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

    private Stage primaryStage;
    private Scene scene;
    private Button button;
    private int target_level;

    private ArrayList<node> NextLayerConnectedNodes = new ArrayList<>();
    private ArrayList<node> LastLayerConnectedNodes = new ArrayList<>();

    private double posX, posY;

    public node(int layer, int index, int type, Stage primaryStage) {
        this.layer = layer;
        this.index = index;
        this.type = type;
        this.is_connected = false;

        if(levelManager == null)
            node.levelManager = new LevelManager(primaryStage); // 保险用 其实要是后面能正常跑的话就删掉吧

        this.primaryStage = levelManager.getPrimaryStage();
        //this.primaryStage = primaryStage;
        this.scene = primaryStage.getScene();

        this.button = new Button();
        button.setPrefSize(config.Map_Node_Width, config.Map_Node_Height);
        //button被按下时触发action()
        button.setOnAction(e -> action());

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GRAY);
        dropShadow.setRadius(5); dropShadow.setOffsetX(2); dropShadow.setOffsetY(2);
        this.button.setEffect(dropShadow); // 添加阴影效果

        if(layer >= All_Nodes.size()){
            All_Nodes.add(new ArrayList<>());
        }

        All_Nodes.get(layer).add(this);
    }

    public void action(){
        load_level();
    }
    public void load_level(){
        levelManager.loadLevel(target_level);
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

    public void set_posX(double posX){
        this.posX = posX;
    }
    public void set_posY(double posY){
        this.posY = posY;
    }
    public void setTarget_level(int target_level){
        this.target_level = target_level;
    }

    //获得绝对坐标
    public double get_left_posX(){
        return button.localToScene(scene.getX(),scene.getY()).getX();
    }
    public double get_up_posY(){
        return button.localToScene(scene.getX(),scene.getY()).getY();
    }

    public double get_posX() {
        return get_left_posX() + (double) config.Map_Node_Width / 2;
    }
    public double get_right_posX(){
        return get_left_posX() + (double)config.Map_Node_Width;
    }
    public double get_posY() {
        return get_up_posY() + (double) config.Map_Node_Height / 2;
    }
    public double get_down_posY(){
        return get_up_posY() + (double)config.Map_Node_Height;
    }

    @Override // 调试用
    public String toString(){
        return String.format("%d-%d, @[%.1f,%.1f]", layer, index, get_posX(), get_posY());
    }

}