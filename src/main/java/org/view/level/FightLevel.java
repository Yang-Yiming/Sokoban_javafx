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

public class FightLevel extends Level {

    private final int id;
    private boolean reverse = false;


    public MapMatrix getMap() {
        return (MapMatrix) map;
    }

    public void init() {
            map = new MapMatrix(mapdata.maps[id]);

        super.init();
    }

    public FightLevel(Pane root, int id, Stage primaryStage, User user, boolean reverse) {
        super(root, primaryStage, user);
        this.id = id;
        this.reverse = reverse;
        map = new MapMatrix(mapdata.maps[id]);
        init();
    }

    @Override
    public void drawMap() {
        root.getChildren().clear(); // 先清空一下地图
        drawBackGround();
        drawPlayer();
        drawBoxesAndWall();
        root.getChildren().add(fadeRectangle);
        if(reverse) {
//            System.out.println(primaryStage.getWidth());
            double leftRootWidth = root.getLayoutBounds().getWidth();
            root.setLayoutX(primaryStage.getWidth() / 2 - leftRootWidth + 200); // 向左移动rightRoot的宽度，实现翻转效果
        }else{
            root.setLayoutX(primaryStage.getWidth() / 2 - 200);
        }
    }
    @Override
    public void createFadeTimeline(){
        //让长方形逐渐变淡
        if(fadeTimeline != null) fadeTimeline.stop();
        fadeTimeline = new Timeline(new KeyFrame(Duration.seconds(0.02), e -> {
            fadeRectangle.setOpacity(fadeRectangle.getOpacity() - 0.02);
            if(fadeRectangle.getOpacity() <= 0){
                fadeRectangle.setOpacity(0);
                fadeTimeline.stop();
                root.getChildren().remove(fadeRectangle);
            }
            drawMap();
        }));
        fadeTimeline.setCycleCount(Animation.INDEFINITE);
        fadeTimeline.play();
    }

}
