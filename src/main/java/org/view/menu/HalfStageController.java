//package org.view.menu;
//
//import javafx.animation.KeyFrame;
//import javafx.animation.KeyValue;
//import javafx.animation.Timeline;
//import javafx.animation.TranslateTransition;
//import javafx.fxml.FXML;
//import javafx.scene.input.MouseEvent;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//
//import java.io.IOException;
//
//public class HalfStageController {
//    protected Stage thisStage;
//    protected Stage MenuStage;
//
//    public void initialize(Stage LoginStage, Stage MenuStage) {
//        this.thisStage = LoginStage;
//        this.MenuStage = MenuStage;
//    }
//
//
//    public void back_to_menu() throws IOException {
//        // 使用TranslateTransition实现向右侧滑出的动画效果
//        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), thisStage.getScene().getRoot());
//        translateTransition.setFromX(0);
//        translateTransition.setToX(thisStage.getWidth());
//
//        translateTransition.setOnFinished(e -> {
//            thisStage.close(); // 播放完后关闭窗口
//        });
//
//        // 让主屏幕变回来
//        Timeline timeline = new Timeline();
//        KeyValue keyValue = new KeyValue(MenuStage.getScene().getRoot().opacityProperty(), 1);
//        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), keyValue);
//        timeline.getKeyFrames().add(keyFrame);
//        timeline.play();
//
//        translateTransition.play();
//    }
//}
