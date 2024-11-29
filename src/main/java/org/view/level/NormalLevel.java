package org.view.level;

import javafx.scene.layout.Pane;

import javafx.stage.Stage;

import org.data.mapdata;
import org.model.MapMatrix;
import org.model.User;

import java.util.Map;

public class NormalLevel extends Level {

    private final int id;
    private boolean default_map = true;

    public MapMatrix getMap() {
        return map;
    }

    public void init() {
        if (default_map)
            map = new MapMatrix(mapdata.maps[id]);

        super.init();
    }

    public NormalLevel(Pane root, int id, Stage primaryStage, User user) {
        super(root, primaryStage, user);
        this.id = id;
        map = new MapMatrix(mapdata.maps[id]);
        init();
    }

    public NormalLevel(Pane root, int[][] map_matrix, Stage primaryStage, int id, User user) {
        super(root, primaryStage, user);
        this.id = id;
        this.default_map = false;
        map = new MapMatrix(map_matrix);
        init();
        this.default_map = true;
    }
}