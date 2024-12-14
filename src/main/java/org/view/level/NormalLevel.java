package org.view.level;

import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;

import org.data.mapdata;
import org.model.InfiniteMap;
import org.model.MapMatrix;
import org.model.Solve.Solve;
import org.model.User;
import org.model.config;

import java.util.Map;

public class NormalLevel extends Level {

    private boolean default_map = true;

    public MapMatrix getMap() {
        return (MapMatrix) map;
    }

    public void init() {
        if (default_map)
            map = new MapMatrix(mapdata.maps[id]);
        solve = new Solve(map);

        double beginTime = System.currentTimeMillis();
        solve.aStarSearch();
        double solve_time = System.currentTimeMillis() - beginTime;

        //用 a* 跑出步数限制 先 +5
        config.this_is_hint = true;
        if(super.stepLimit == -1) super.stepLimit = solve_moves(new MapMatrix(mapdata.maps[id])).length() + 5;
        config.this_is_hint = false;

        LevelManager.groupNumber = id / 5 + 1;

        super.init();

        if(solve_time > 50 && config.auto_check_fail) {
            config.auto_check_fail = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText("地图过于复杂，已关闭A*判负");
            alert.setContentText("A*用时" + solve_time + "ms");
            alert.show();
        }
    }

    public NormalLevel(Pane root, int id, int[][][] maps, Stage primaryStage, User user) {
        super(root, primaryStage, user);
        this.id = id;
        map = new MapMatrix(maps[id]);
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