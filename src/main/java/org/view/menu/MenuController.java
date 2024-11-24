package org.view.menu;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.model.User;
import org.view.level.LevelManager;

import java.io.IOException;

public class MenuController {

    private Pane root;
    private Stage primaryStage;
    private Scene scene;
    private User user = null;

    public MenuController() throws IOException {
    }

    public void initialize(Pane root, Stage primaryStage) {
        this.root = root;
        this.primaryStage = primaryStage;
    }

    @FXML
    private Button Login;

    @FXML
    private Button Settings;

    @FXML
    private Button StartButton;



    @FXML
    void LoginButtonClicked(MouseEvent event) throws IOException {
        try{
            // 加载登陆窗口fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            LoginController loginController = loader.getController();
            Scene scene  = new Scene(root);

            // 创建一个新的stage用于显示登陆窗口
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.TRANSPARENT);
            loginStage.initModality(Modality.APPLICATION_MODAL);
            // 将登陆窗口的stage传给controller
            loginController.initialize(loginStage, primaryStage, user);

            ChangeStageAnim(scene, loginStage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void SettingButtonClicked(MouseEvent event) {

    }

    private void ChangeStageAnim(Scene scene, Stage newStage) { // 用于login 和 setting这两个从右侧滑过来的模块
        // 将背景设置为透明
        scene.setFill(null);
        newStage.setScene(scene);
        newStage.show();

        // 获取当前主窗口的宽度和高度
        double mainStageWidth = primaryStage.getWidth();
        double mainStageHeight = primaryStage.getHeight();

        int ybias = 28; // 顶部栏高度
        // 设置登录窗口的初始位置在主窗口位置
        newStage.setX(primaryStage.getX() + mainStageWidth / 2);
        newStage.setY(primaryStage.getY() + ybias);

        // 设置登录窗口的大小为半个主窗口大小
        newStage.setWidth(mainStageWidth / 2);
        newStage.setHeight(mainStageHeight - ybias);

        // 使用TranslateTransition实现从右侧滑入的动画效果
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), newStage.getScene().getRoot());
        translateTransition.setFromX(newStage.getWidth());
        translateTransition.setToX(0);
        translateTransition.play();

        // 让主屏幕变淡
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(primaryStage.getScene().getRoot().opacityProperty(), 0.2);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    @FXML
    void StartButtonClicked(MouseEvent event) {
        if(user == null){
            StartButton.setText("Please Login First");//非常简陋，以后美化（）
//            return; // 这个 return 挡住我测试的路，先注释掉
        }
        LevelManager levelManager = new LevelManager(primaryStage);
        levelManager.setUser(user);
        levelManager.start();
    }

}
