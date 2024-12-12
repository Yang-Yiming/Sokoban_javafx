package org.view.level;

import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.data.mapdata;
import org.model.InfiniteMap;
import org.model.MapMatrix;
import org.model.Solve.Solve;
import org.model.User;

public class InfiniteLevel extends Level {

    private final int id;
    private boolean default_map = true;

    public InfiniteMap getMap() {
        return (InfiniteMap) map;
    }

    public void super_init() {
        super.init();
        this.user = user;
//        load_gui(user);
    }

    public void init() {
        start_time = System.currentTimeMillis();
        level_past = 0;
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
        sublevel_begin_x = getMap().getSublevel_begin_x();
        sublevel_begin_y = getMap().getSublevel_begin_y();

        root.getChildren().clear(); // 先清空一下地图
        drawGrass();
        drawButterflyShadow();
        drawBackGround();
        drawPlayer();
        drawBoxesAndWall();
        drawButterfly();
//        drawGUI();
        drawTime();
    }
    Text timeText, pastText;
    long start_time;
    public int level_past = 0;
    public void drawTime(){
        //计算当前时间
        long time = System.currentTimeMillis() - start_time;

        //显示当前时间
        timeText = new Text("用时：" + time / 1000 + " s");
        timeText.setFont(new Font(pixelFont.getName(), 25));
        timeText.setFill(Color.web("#55371d"));
        timeText.setLayoutX(20);
        timeText.setLayoutY(40);
        root.getChildren().add(timeText);

        pastText = new Text("过关：" + level_past);
        pastText.setFont(new Font(pixelFont.getName(), 25));
        pastText.setFill(Color.web("#55371d"));
        pastText.setLayoutX(20);
        pastText.setLayoutY(70);
        root.getChildren().add(pastText);

        Text escText = new Text("按 ESC 键结束游戏");
        escText.setFont(new Font(pixelFont.getName(), 15));
        escText.setFill(Color.web("#55371d"));
        escText.setLayoutX(20);
        escText.setLayoutY(100);
        root.getChildren().add(escText);
    }

    @Override
    public boolean isWin() {
        for (int y = sublevel_begin_y; y < sublevel_begin_y + getMap().getWin_check_height(); ++y)
            for (int x = sublevel_begin_x; x < getMap().getRight_boundary() + getMap().getWin_check_width(); ++x)
                if (map.hasGoal(x, y) && !map.hasBox(x, y))
                    return false;
        return true;
    }

    @Override
    public char solve_next_move() {
        return ' '; // 因为不会写
    }

}