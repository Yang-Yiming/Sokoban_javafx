package org.view.menu;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.model.User;
import org.view.level.InfiniteLevelManager;
import org.view.level.LevelManager;
import org.view.level.FightLevelManager;

import java.io.IOException;

public class MenuController {

    private Pane root;
    private Stage primaryStage;
    private Scene scene;
    private User user;

    public MenuController() throws IOException {
    }

    public void initialize(Pane root, Stage primaryStage) {
        this.root = root;
        this.primaryStage = primaryStage;
        user = new User("",""); // 初始化一个空的user 不然login无法更改他
    }

//    @FXML
//    private Button Login;
//
//    @FXML
//    private Button Settings;
//
//    @FXML
//    private Button StartButton;

//    @FXML
//    void LoginButtonClicked() {
//        try{
//            // 加载登陆窗口fxml
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
//            Parent root = loader.load();
//            LoginController loginController = loader.getController();
//            Scene scene  = new Scene(root);
//
//            // 创建一个新的stage用于显示登陆窗口
//            Stage loginStage = new Stage();
//            loginStage.initStyle(StageStyle.TRANSPARENT);
//            loginStage.initModality(Modality.APPLICATION_MODAL);
//            // 将登陆窗口的stage传给controller
//            loginController.initialize(loginStage, primaryStage, this);
//
//            ChangeStageAnim(scene, loginStage, primaryStage);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void open_settings(Stage primaryStage) {
//        try{
//            // 加载设置窗口fxml
//            FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("/fxml/Settings.fxml"));
//            Parent root = loader.load();
//            SettingController settingController = loader.getController();
//            Scene scene = new Scene(root);
//
//            // 创建一个新的stage用于显示设置窗口
//            Stage settingStage = new Stage();
//            settingStage.initStyle(StageStyle.TRANSPARENT);
//            settingStage.initModality(Modality.APPLICATION_MODAL);
//            // 将设置窗口的stage传给controller
//            settingController.initialize(settingStage, primaryStage);
//
//            ChangeStageAnim(scene, settingStage, primaryStage);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @FXML
//    public void SettingButtonClicked() {
//        open_settings(primaryStage);
//    }

    public static void ChangeStageAnim(Scene scene, Stage newStage, Stage primaryStage) { // 用于login 和 setting这两个从右侧滑过来的模块
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

//    @FXML
    public void StartButtonClicked(MenuView menuView) {
        if(user == null || user.getName().isEmpty()){ // 好蠢的判断

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("请先登陆");
            alert.setHeaderText("请先登陆，或以游客模式继续。游客模式下存档功能将会失效。");
            alert.setContentText("是否以游客模式继续？");
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if(result == ButtonType.OK) {
                // user = new User("guest", "");
            } else {
                return;
            }
        }
        menuView.setMusic("classic.m4a");
        LevelManager levelManager = new LevelManager(primaryStage, this);
        levelManager.setUser(user);
        levelManager.start();
    }

    public void startInfiniteLevel() {
        InfiniteLevelManager manager = new InfiniteLevelManager(primaryStage, this);
        manager.setUser(user);
        manager.start();
        primaryStage.show();
    }

    public void startFightLevel(){
         FightLevelManager manager = new FightLevelManager(primaryStage, this);
         manager.start();
         primaryStage.show();
    }

    public User get_user(){
        return user;
    }
    public void set_user(User user){
        this.user = user;
    }

}
