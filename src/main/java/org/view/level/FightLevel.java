package org.view.level;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.data.mapdata;
import org.model.MapMatrix;
import org.model.User;

public class FightLevel extends Level {

    private final int id;

    public MapMatrix getMap() {
        return (MapMatrix) map;
    }

    public void init() {
            map = new MapMatrix(mapdata.maps[id]);

        super.init();
    }

    public FightLevel(Pane root, int id, Stage primaryStage, User user) {
        super(root, primaryStage, user);
        this.id = id;
        map = new MapMatrix(mapdata.maps[id]);
        init();
    }

    public FightLevel(Pane root, int[][] map_matrix, Stage primaryStage, int id, User user) {
        super(root, primaryStage, user);
        this.id = id;
        map = new MapMatrix(map_matrix);
        init();
    }
}
