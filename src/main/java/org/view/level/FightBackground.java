package org.view.level;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.data.mapdata;
import org.model.MapMatrix;
import org.model.User;
import org.model.config;
import org.view.VisualEffects.GlowRectangle;

public class FightBackground extends Level {

    private final int id;


    public MapMatrix getMap() {
        return (MapMatrix) map;
    }

    public void init() {
        map = new MapMatrix(mapdata.maps[id]);

        super.init();
    }

    public FightBackground(Pane root, int id, Stage primaryStage, User user) {
        super(root, primaryStage, user);
        this.id = id;
        map = new MapMatrix(mapdata.maps[id]);
        init();
    }

    @Override
    public void drawMap() {
        root.getChildren().clear(); // 先清空一下地图
        drawGrass();
        drawButterflyShadow();
        drawButterfly();
//        root.getChildren().add(fadeRectangle);
    }

}
