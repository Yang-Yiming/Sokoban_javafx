package org.view.level;

import javafx.scene.layout.Pane;

import javafx.stage.Stage;

import org.data.mapdata;
import org.model.InfiniteMap;
import org.model.MapMatrix;
import org.model.User;

public class InfiniteLevel extends Level {

    private final int id;
    private boolean default_map = true;

    public InfiniteMap getMap() {
        return (InfiniteMap) map;
    }

    public void super_init() {
        super.init();
        load_gui(user);
    }

    public void init() {
        if (default_map)
            map = new InfiniteMap(mapdata.maps[id]);

        super_init();
    }

    public InfiniteLevel(Pane root, int id, Stage primaryStage, User user) {
        super(root, primaryStage, user);
        this.id = id;
        map = new InfiniteMap(mapdata.maps[id]);
        init();
    }
    public InfiniteLevel(Pane root, int[][] map_matrix, Stage primaryStage, int id, User user) {
        super(root, primaryStage, user);
        this.id = id;
        this.default_map = false;
        map = new InfiniteMap(map_matrix);
        init();
        this.default_map = true;
    }

    @Override
    public void drawMap() {
        begin_x = getMap().getLeft_boundary();
        begin_y = getMap().getUp_boundary();
        sublevel_begin_x = getMap().getSublevel_begin_x();
        sublevel_begin_y = getMap().getSublevel_begin_y();

        root.getChildren().clear(); // 先清空一下地图
        drawGrass();
        drawButterflyShadow();
        drawBackGround();
        drawPlayer();
        drawBoxesAndWall();
        drawButterfly();
        drawGUI();
        //输出 boxes 数量
//        System.out.println("boxes: " + boxes.size());
    }

}