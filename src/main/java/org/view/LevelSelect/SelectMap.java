package org.view.LevelSelect;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.data.mapdata;

import java.util.ArrayList;

public class SelectMap {
    private double AnchorX, AnchorY;
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

    public void init(){
        add_levels(mapdata.maps);
    }
    void add_levels(int[][][] maps){
        //for(int i = 0; i <)
    }



}
