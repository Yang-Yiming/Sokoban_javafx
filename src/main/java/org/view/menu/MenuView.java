package org.view.menu;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.model.Rand;
import org.model.SavingManager;
import org.model.User;
import org.model.config;
import org.view.level.Grass;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MenuView extends AnchorPane {
    private Button startButton;
    private Button loginButton;
    private Button settingsButton;
    MenuController menuController;
    ImageView title = null;

    private void createTitle(){
        title = new ImageView(new Image(getClass().getResourceAsStream("/images/title.gif"))); //有 gif 了
        title.setLayoutX(200.0);
        title.setLayoutY(50.0);
        title.setFitHeight(200.0);
        title.setFitWidth(400.0);
        title.setPreserveRatio(true);
        double centerX = title.getLayoutX() - title.getFitWidth() / 2;
        double centerY = title.getLayoutY() - title.getFitHeight() / 2;
        title.setTranslateX(centerX);
        title.setTranslateY(centerY);

        // Add rotate animation
        // 太可爱了建议保留
//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.ZERO,
//                        new KeyValue(title.rotateProperty(), 0),
//                        new KeyValue(title.fitWidthProperty(), 1 * title.getFitWidth()),
//                        new KeyValue(title.fitHeightProperty() , 1 * title.getFitHeight())),
//                new KeyFrame(Duration.millis(5000),
//                        new KeyValue(title.rotateProperty(), -4),
//                        new KeyValue(title.fitWidthProperty(), 1.2 * title.getFitWidth()),
//                        new KeyValue(title.fitHeightProperty() , 1.2 * title.getFitHeight())),
//                new KeyFrame(Duration.millis(10000),
//                        new KeyValue(title.rotateProperty(), 0),
//                        new KeyValue(title.fitWidthProperty(), 1 * title.getFitWidth()),
//                        new KeyValue(title.fitHeightProperty() , 1 * title.getFitHeight())),
//                new KeyFrame(Duration.millis(15000),
//                        new KeyValue(title.rotateProperty(), 4),
//                        new KeyValue(title.fitWidthProperty(), 1.2 * title.getFitWidth()),
//                        new KeyValue(title.fitHeightProperty() , 1.2 * title.getFitHeight())),
//                new KeyFrame(Duration.millis(20000),
//                        new KeyValue(title.rotateProperty(), 0),
//                        new KeyValue(title.fitWidthProperty(), 1 * title.getFitWidth()),
//                        new KeyValue(title.fitHeightProperty() , 1 * title.getFitHeight()))
//        );
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();

    }

    Font pixelFont = Font.loadFont(getClass().getResource("/font/pixel.ttf").toExternalForm(), 30);

    private void createButtons(){

        startButton = generate_button("开始", (getPrefWidth() - 100) / 2, 310);
        startButton.setFont(pixelFont);
        startButton.setOnMouseClicked(event -> startButtonClicked());
        startButton.setOnKeyPressed(event -> {
            if(event.getCode().toString().equals("ENTER")) startButtonClicked();
        });

        loginButton = new Button();
        ImageView loginImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/login.png")));
        loginImageView.setFitWidth(30);
        loginImageView.setFitHeight(30);
        loginButton.setGraphic(loginImageView);
        loginButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px;");
        loginButton.setLayoutX(700.0);
        loginButton.setLayoutY(35.0);
        loginButton.setMaxSize(30, 30);
        loginButton.setOnMouseClicked(event -> loginButtonClicked());
        //鼠标放上去时变成 login_clicked.png
        loginButton.setOnMouseEntered(event -> {
            loginImageView.setImage(new Image(getClass().getResourceAsStream("/images/login_clicked.png")));
        });
        loginButton.setOnMouseExited(event -> {
            loginImageView.setImage(new Image(getClass().getResourceAsStream("/images/login.png")));
        });

        Settings settings = new Settings();
        settingsButton = settings.createButton(this);

        //主题选择？
        Theme theme = new Theme();
        themeButton = theme.createButton(this);
    }
    Button themeButton;
    Timeline cloudsTimeLine = null;
    double clouds0Pos = -1000, clouds1Pos = 0, clouds2Pos = -400, clouds3Pos = 200, clouds4Pos = -400, clouds5Pos = -200;
    private ImageView clouds0 = new ImageView(new Image(getClass().getResourceAsStream("/images/clouds/Clouds0.png"), 1600, 800, false, false));
    private ImageView clouds1 = new ImageView(new Image(getClass().getResourceAsStream("/images/clouds/Clouds1.png"), 1800, 900, false, false));
    private ImageView clouds2 = new ImageView(new Image(getClass().getResourceAsStream("/images/clouds/Clouds2.png"), 1300, 500, false, false));
    private ImageView clouds3 = new ImageView(new Image(getClass().getResourceAsStream("/images/clouds/Clouds3.png"), 1000, 300, false, false));
    private ImageView clouds4 = new ImageView(new Image(getClass().getResourceAsStream("/images/clouds/Clouds4.png"), 1000, 200, false, false));
    private ImageView clouds5 = new ImageView(new Image(getClass().getResourceAsStream("/images/clouds/Clouds5.png"), 600, 300, false, false));
    private void createClouds(){
        if(cloudsTimeLine == null) {
            cloudsTimeLine = new Timeline(new KeyFrame(Duration.millis(10), event -> {
                clouds0Pos -= 0.25;
                if(clouds0Pos < -1600) clouds0Pos = getWidth();
                clouds0.setLayoutX(clouds0Pos);
                clouds1Pos -= 0.1;
                if(clouds1Pos < -1600) clouds1Pos = getWidth();
                clouds1.setLayoutX(clouds1Pos);
                clouds2Pos -= 0.2;
                if(clouds2Pos < -1300) clouds2Pos = getWidth();
                clouds2.setLayoutX(clouds2Pos);
                clouds3Pos -= 0.3;
                if(clouds3Pos < -1000) clouds3Pos = getWidth();
                clouds3.setLayoutX(clouds3Pos);
                clouds4Pos -= 0.5;
                if(clouds4Pos < -1000) clouds4Pos = getWidth();
                clouds4.setLayoutX(clouds4Pos);
                clouds5Pos -= -0.7;
                if(clouds5Pos < -600) clouds5Pos = getWidth();
            }));
            cloudsTimeLine.setCycleCount(Timeline.INDEFINITE);
            cloudsTimeLine.play();
            getChildren().add(clouds0);
            getChildren().add(clouds1);
            getChildren().add(clouds2);
            getChildren().add(clouds3);
            getChildren().add(clouds4);
        }
    }

    public static ArrayList<Rectangle> grass = new ArrayList<>();
    public static ArrayList<Integer> grassI = new ArrayList<>();
    private ImageView box;
    double boxV = 0, boxA = 0.15;
    Timeline boxTimeline;
    private void createBox(){
        box = new ImageView(new Image(getClass().getResourceAsStream("/images/box_2d.png"), 50, 50, false, false));
        box.setOnMouseDragged(event -> {
            box.setX(event.getX() - 25);
            box.setY(event.getY() - 25);
        });
        getChildren().add(box);
        box.setOnMousePressed(event -> boxTimeline.pause());
        box.setOnMouseReleased(event -> boxTimeline.play());
        boxTimeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            if(box.getY() < getHeight() - 100){
                boxV += boxA;
                box.setY(box.getY() + boxV);
            }else{
                boxV = 0;
                box.setY(getHeight() - 100);
            }
        }));
        boxTimeline.setCycleCount(Timeline.INDEFINITE);
        boxTimeline.play();
    }
    Image cat_stand = new Image(getClass().getResourceAsStream("/images/player_cat/cat_stand.gif"), 50, 50, false, false);
    Image cat_run = new Image(getClass().getResourceAsStream("/images/player_cat/cat_run.gif"), 50, 50, false, false);
    ImageView cat;
    Timeline catTimeline;
    double catV = 0, catA = 0.15;
    public static MediaPlayer mediaPlayer;
    private void createCat(){
        cat = new ImageView(cat_stand);
        cat.setX(50);
        cat.setY(getPrefHeight() - 86);
        getChildren().add(cat);

        catTimeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            if(cat.getY() < getHeight() - 86){
                catV += catA;
                cat.setY(cat.getY() + catV);
            }else{
                catV = 0;
                cat.setY(getHeight() - 86);
            }
            if(cat.getX() < box.getX() - 50){
                cat.setImage(cat_run);
                cat.setScaleX(1);
                cat.setX(cat.getX() + 2);
            }else if(cat.getX() > box.getX() + 50){
                cat.setImage(cat_run);
                cat.setScaleX(-1);
                cat.setX(cat.getX() - 2);
            }else{
                cat.setImage(cat_stand);
            }
        }));
        catTimeline.setCycleCount(Timeline.INDEFINITE);
        catTimeline.play();
    }
    private void addground(){
        grass = new ArrayList<>();
        grassI = new ArrayList<>();
        for(int i = 0; i < 40; ++i){
//            System.out.println(getPrefHeight());
            Rectangle rect = new Rectangle(i * 50, getPrefHeight() - 50, 50, 50);
            grass.add(rect);
            grassI.add(i);
            int divide = 10;
            double siz = 50 / divide;
            for(int j = 0; j < divide; ++j){
                double height = siz * Rand.myRand(i, j, 0, -1, 3);
                if(height <= 0) continue;
                height += 50;
                Rectangle rect1 = new Rectangle(siz, height);
                rect1.setX(i * 50 + j * siz);
                rect1.setY(getPrefHeight() - height);
                grass.add(rect1);
                grassI.add(i);
//                    System.out.println(rect1.getY());
                rect1.setFill(Grass.randColor(i, i));
                getChildren().add(rect1);
//                    ++j;
            }
            rect.setFill(Grass.randColor(i, i));
            getChildren().add(rect);
        }
    }

    //    private Rectangle grass;
    public MenuView(MenuController menuController){
        this.menuController = menuController;
        setPrefHeight(600.0);
        setPrefWidth(800.0);
        setMaxHeight(600.0);
        setMaxWidth(800.0);
        setMinHeight(600.0);
        setMinWidth(800.0);

        //设置背景颜色
        setStyle("-fx-background-color: linear-gradient(to bottom, #b0d5df, #dfecd5);");
        createClouds();

        createBox();
        createCat();

        //在最下方铺一层绿色的草地
//        grass = new Rectangle(0, getHeight() - 100, getWidth(), 100);
//        grass.setFill(Color.rgb(124, 153, 32));
//        getChildren().add(grass);
        addground();

        if(mediaPlayer == null) mediaPlayer = new MediaPlayer(new Media(getClass().getResource("/music/main.m4a").toExternalForm()));

//        System.out.println("play!");
        mediaPlayer.play();
        setMusic("main.m4a");

        createTitle();
        addScreenSizeListener();
        createButtons();
        changePosX(getPrefWidth());
        changePosY(getPrefHeight());
        getChildren().addAll(title, startButton, loginButton, settingsButton, themeButton);
        //重新设置宽度和高度
    }

    private void addScreenSizeListener(){
        widthProperty().addListener((observable, oldValue, newValue) -> {
            changePosX(newValue.doubleValue());
            changeBtnPosX(newValue.doubleValue());
        });
        heightProperty().addListener((observable, oldValue, newValue) -> {
            changePosY(newValue.doubleValue());
        });
    }
    void changePosX(double doubleValue){
        //将 title 居中靠上
        title.setLayoutX((doubleValue - title.getFitWidth()) / 2);
        //将 btn1,2,3 居中
        if(startButton != null) startButton.setLayoutX((doubleValue - 100) / 2);
        if(loginButton != null) loginButton.setLayoutX(doubleValue - 150);
        if(settingsButton != null) settingsButton.setLayoutX(doubleValue - 80);
        if(themeButton != null) themeButton.setLayoutX(doubleValue - 220);
        if(shade != null) shade.setWidth(doubleValue);
    }
    void changePosY(double doubleValue){
//        grass.setY(doubleValue - 100);
        clouds0.setLayoutY(doubleValue - 600);
        clouds1.setLayoutY(doubleValue - 800);
        clouds2.setLayoutY(doubleValue - 400);
        clouds3.setLayoutY(doubleValue - 600);
        clouds4.setLayoutY(doubleValue - 700);
        clouds5.setLayoutY(doubleValue - 600);
        for(Rectangle rect : grass){
            rect.setY(doubleValue - rect.getHeight());
        }
        if(shade != null) shade.setHeight(doubleValue);
    }
    void changeBtnPosX(double doubleValue){
        if(btn_mode1 != null) btn_mode1.setLayoutX((doubleValue - 180) / 2);
        if(btn_mode2 != null) btn_mode2.setLayoutX((doubleValue - 180) / 2);
        if(btn_mode3 != null) btn_mode3.setLayoutX((doubleValue - 180) / 2);
    }

    private ScaleTransition createScaleTransition(Button button, double size1, double size2, Duration duration) {
        ScaleTransition scaleTransition = new ScaleTransition(duration, button);
        scaleTransition.setFromX(size1);
        scaleTransition.setFromY(size1);
        scaleTransition.setToX(size2);
        scaleTransition.setToY(size2);
        scaleTransition.setCycleCount(1);
        scaleTransition.setAutoReverse(true); // 动画结束后自动反向播放
        return scaleTransition;
    }

    private Button generate_button(String text, double x, double y) {
        Button btn = new Button(text);
        btn.setLayoutX(x - btn.getPrefWidth() / 2);
        btn.setLayoutY(y - btn.getPrefHeight() / 2);
        btn.setPrefHeight(57.0);
        // btn.setPrefWidth(161.0);
        btn.setFont(new Font(30.0));
        btn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px; -fx-text-fill: #55371d;");
        ScaleTransition scaleUp = createScaleTransition(btn, 1, config.font_size_2 / config.font_size_1, Duration.millis(config.font_size_change_millis));
        ScaleTransition scaleDown = createScaleTransition(btn, config.font_size_2 / config.font_size_1, 1, Duration.millis(config.font_size_change_millis));

        btn.setOnMouseEntered(event -> scaleUp.play());

        // 鼠标离开事件
        btn.setOnMouseExited(event -> scaleDown.play());

        return btn;
    }
    Button btn_mode1 = null, btn_mode2 = null, btn_mode3 = null;
    Timeline volumeUp;
    public void setMusic(String music){
        mediaPlayer.stop();
        Media backgroundMusic = new Media(getClass().getResource("/music/" + music).toExternalForm());
        mediaPlayer = new MediaPlayer(backgroundMusic);
        mediaPlayer.setVolume(0);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        if(volumeUp != null) volumeUp.stop();
        volumeUp = new Timeline(
                new KeyFrame(Duration.seconds(5), new KeyValue(mediaPlayer.volumeProperty(), config.volume))
        );
        volumeUp.play();
//        System.out.println("set music");
    }
    private void startButtonClicked() {
        // 按钮生成
        btn_mode1 = generate_button("经典模式", 300, 270);
        btn_mode2 = generate_button("无尽模式", 300, 320);
        btn_mode3 = generate_button("双人模式", 300, 370);
        btn_mode1.setFont(pixelFont);
        btn_mode2.setFont(pixelFont);
        btn_mode3.setFont(pixelFont);
        changeBtnPosX(getWidth());
        getChildren().addAll(btn_mode1, btn_mode2, btn_mode3);

        // 动画
        FadeTransition btn1_transition = generate_fade_transition(btn_mode1,0.2, 0.0, 1.0);
        FadeTransition btn2_transition = generate_fade_transition(btn_mode2,0.2, 0.0, 1.0);
        FadeTransition btn3_transition = generate_fade_transition(btn_mode3,0.2, 0.0, 1.0);

        FadeTransition fadeTransition = generate_fade_transition(startButton,0.2, 1.0, 0.0);
        fadeTransition.setOnFinished(event -> {
            getChildren().remove(startButton);
            btn1_transition.play();
            btn2_transition.play();
            btn3_transition.play();
        });
        // Handle start button click
        btn_mode1.setOnMouseClicked(event -> {config.tile_size = 55; config.mode = 1; menuController.StartButtonClicked(this);});
        btn_mode2.setOnMouseClicked(event -> {config.tile_size = 55; setMusic("inf.m4a"); config.mode = 2; menuController.startInfiniteLevel();});
        btn_mode3.setOnMouseClicked(event -> {setMusic("fight.m4a"); config.mode = 3; menuController.startFightLevel();});
    }

    private FadeTransition generate_fade_transition(Button button, double duration, double from, double to) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), button);
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        fadeTransition.play();
        return fadeTransition;
    }
    private boolean haveUser(){
        if(menuController.get_user() != null && !menuController.get_user().getName().isEmpty()) return true;
        return false;
    }
    Rectangle shade;
    ImageView paper = new ImageView(new Image(getClass().getResourceAsStream("/images/paper.png"), 500, 500, false, false));

    VBox loginVbox;
    HBox usernameHbox, passwordHbox, confirmPasswordHbox, reminderHbox, buttonsHbox;
    TextField usernameInput;
    PasswordField passwordInput, confirmPasswordInput;
    Text reminderText, loginText, maxLevel, maxDifficulty, minTime;
    Button close;
    private void loginButtonClicked() {
        // Handle login button click
//        menuController.LoginButtonClicked();
        //变暗
        shade = new Rectangle(0, 0, getWidth(), getHeight());
        shade.setFill(Color.rgb(0, 0, 0, 0.5));
        getChildren().add(shade);
        //纸
        paper.setX(150);
        paper.setY(50);
        getChildren().add(paper);
        if(haveUser()){
            //名字居中
            loginText = new Text(400 - menuController.get_user().getName().length() * 11, 150.0, menuController.get_user().getName());
            loginText.setFont(new Font(pixelFont.getName(), 45));
            loginText.setFill(javafx.scene.paint.Color.web("#55371d"));
            getChildren().add(loginText);

            //最大关卡数
            maxLevel = new Text(300 - (menuController.get_user().getMaxLevel() + "").length() * 11, 200.0, "最高关卡数: " + menuController.get_user().getMaxLevel());
            maxLevel.setFont(new Font(pixelFont.getName(), 20));
            maxLevel.setFill(javafx.scene.paint.Color.web("#55371d"));
            getChildren().add(maxLevel);

            //最高难度+关卡
            maxDifficulty = new Text(300 - (menuController.get_user().getMaxDifficultyLevel() + "").length() * 11, 250.0, "最高得分(难度+关卡数): " + menuController.get_user().getMaxDifficultyLevel());
            maxDifficulty.setFont(new Font(pixelFont.getName(), 20));
            maxDifficulty.setFill(javafx.scene.paint.Color.web("#55371d"));
            getChildren().add(maxDifficulty);

            //最短平均用时
            minTime = new Text(311 - (menuController.get_user().getMinTime() + "").length() * 11, 300.0, "最短平均用时: " + (menuController.get_user().getMinTime() > 0 ? menuController.get_user().getMinTime(): "无"));
            minTime.setFont(new Font(pixelFont.getName(),20));
            minTime.setFill(javafx.scene.paint.Color.web("#55371d"));
            getChildren().add(minTime);

            //退出登录
            Button logout = new Button("Logout");
            logout.setFont(new Font(pixelFont.getName(), 20));
            logout.setStyle("-fx-background-color: transparent; -fx-border-color: #55371d; -fx-border-width: 2px; -fx-text-fill: #55371d;");
            logout.setLayoutX(350);
            logout.setLayoutY(350);
            logout.setOnMouseClicked(event -> {
                menuController.set_user(new User("", ""));
                getChildren().remove(shade);
                getChildren().remove(paper);
                getChildren().remove(loginText);
                getChildren().remove(close);
                getChildren().remove(logout);
                getChildren().remove(maxLevel);
                getChildren().remove(maxDifficulty);
                getChildren().remove(minTime);
                loginButtonClicked();
            });
            getChildren().add(logout);

            //X
            close = new Button();
            Image X = new Image(getClass().getResourceAsStream("/images/X.png"), 30, 30, false, false);
            close.setGraphic(new ImageView(X));
            getChildren().add(close);
            close.setLayoutX(630);
            close.setLayoutY(60);
            close.setMaxSize(30, 30);
            close.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px; -fx-text-fill: #55371d;");
            //点击关闭按钮
            close.setOnMouseClicked(event -> {
                getChildren().remove(shade);
                getChildren().remove(paper);
                getChildren().remove(loginText);
                getChildren().remove(close);
                getChildren().remove(logout);
                getChildren().remove(maxLevel);
                getChildren().remove(maxDifficulty);
                getChildren().remove(minTime);
            });
            return;
        }
        //标题
        loginText = new Text(340.0, 150.0, "Login");
        loginText.setFont(new Font(pixelFont.getName(), 45));
        loginText.setFill(javafx.scene.paint.Color.web("#55371d"));
        getChildren().add(loginText);
        //按钮

        // 创建VBox
        loginVbox = new VBox(25);
        loginVbox.setPrefHeight(214.0);
        loginVbox.setPrefWidth(209.0);
        loginVbox.setLayoutX(260);
        loginVbox.setLayoutY(200);

        // 创建用户名HBox
        usernameHbox = new HBox(10); // 间距为10
        usernameHbox.setAlignment(Pos.CENTER_LEFT);
        Text usernameText = new Text("Username ");
        usernameText.setFill(javafx.scene.paint.Color.web("#55371d"));
        usernameText.setFont(new Font(pixelFont.getName(), 25));
        usernameInput = new TextField();
        usernameInput.setPrefWidth(150);
        usernameInput.setMaxWidth(150);
        usernameInput.setMinWidth(150);
        //将输入框改为透明，边框是棕色
        usernameInput.setStyle("-fx-background-color: transparent; -fx-border-color: #55371d; -fx-border-width: 2px;");

        HBox.setHgrow(usernameInput, Priority.NEVER);
        usernameHbox.getChildren().addAll(usernameText, usernameInput);
        loginVbox.getChildren().add(usernameHbox);

        // 创建密码HBox
        passwordHbox = new HBox(10);
        passwordHbox.setAlignment(Pos.CENTER_LEFT);
        Text passwordText = new Text("Password ");
        passwordText.setFill(javafx.scene.paint.Color.web("#55371d"));
        passwordText.setFont(new Font(pixelFont.getName(), 25));
        passwordInput = new PasswordField();
        passwordInput.setMaxWidth(150);
        passwordInput.setMaxWidth(150);
        passwordInput.setMinWidth(150);
        passwordInput.setStyle("-fx-background-color: transparent; -fx-border-color: #55371d; -fx-border-width: 2px;");

        HBox.setHgrow(passwordInput, Priority.NEVER);
        passwordHbox.getChildren().addAll(passwordText, passwordInput);
        loginVbox.getChildren().add(passwordHbox);

        // 创建确认密码HBox
        confirmPasswordHbox = new HBox(10);
        Text confirmPasswordText = new Text("确认密码   ");
        confirmPasswordText.setFill(javafx.scene.paint.Color.web("#55371d"));
        confirmPasswordText.setFont(new Font(pixelFont.getName(), 20));
        confirmPasswordInput = new PasswordField();
        confirmPasswordInput.setMaxWidth(150);
        confirmPasswordInput.setMaxWidth(150);
        confirmPasswordInput.setMinWidth(150);
        confirmPasswordInput.setStyle("-fx-background-color: transparent; -fx-border-color: #55371d; -fx-border-width: 2px;");

        HBox.setHgrow(confirmPasswordInput, Priority.NEVER);
        confirmPasswordHbox.getChildren().addAll(confirmPasswordText, confirmPasswordInput);
//        loginVbox.getChildren().add(confirmPasswordHbox);

        // 创建提醒HBox
        reminderHbox = new HBox(10);
        reminderText = new Text("");
        reminderText.setFill(javafx.scene.paint.Color.web("#55371d"));
        reminderText.setFont(new Font(pixelFont.getName(), 15));
        reminderHbox.getChildren().addAll(reminderText);
        loginVbox.getChildren().add(reminderHbox);

        // 创建按钮HBox
        buttonsHbox = new HBox(10);
        buttonsHbox.setAlignment(Pos.CENTER_LEFT);
        Button loginButton = new Button("Login");
        loginButton.setFont(new Font(pixelFont.getName(), 15));
        loginButton.setOnAction(event -> handleLogin());
        loginButton.setStyle("-fx-background-color: transparent; -fx-border-color: #55371d; -fx-border-width: 2px; -fx-text-fill: #55371d;");
        Button registerButton = new Button("Register");
        registerButton.setFont(new Font(pixelFont.getName(), 15));
        registerButton.setStyle("-fx-background-color: transparent; -fx-border-color: #55371d; -fx-border-width: 2px; -fx-text-fill: #55371d;");
        registerButton.setOnAction(event -> {
            try {
                handleRegister();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // 你可以在这里添加更多的错误处理逻辑
            }
        });
        buttonsHbox.getChildren().addAll(loginButton, registerButton);
        loginVbox.getChildren().add(buttonsHbox);

        getChildren().add(loginVbox);

        //关闭
        close = new Button();
        Image X = new Image(getClass().getResourceAsStream("/images/X.png"), 30, 30, false, false);
        close.setGraphic(new ImageView(X));
        getChildren().add(close);
        close.setLayoutX(630);
        close.setLayoutY(60);
        close.setMaxSize(30, 30);
        close.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px; -fx-text-fill: #55371d;");
        //点击关闭按钮
        close.setOnMouseClicked(event -> {
            getChildren().remove(shade);
            getChildren().remove(paper);
            getChildren().remove(loginText);
            getChildren().remove(close);
            getChildren().remove(loginVbox);
        });

    }
    private String UserName, Password;
    public void handleLogin() {
        if(haveUser()) return;
        loginText.setText("Login");
        loginText.setFont(new Font(pixelFont.getName(), 45));
        loginText.setX(340.0);
        loginVbox.getChildren().remove(reminderHbox);
        if(loginVbox.getChildren().contains(confirmPasswordHbox)){
            loginVbox.getChildren().remove(confirmPasswordHbox);
            usernameInput.setText(""); passwordInput.setText("");
            return;
        }

        UserName = usernameInput.getText();
        Password = passwordInput.getText();

        SavingManager.read();
        int userid = SavingManager.getUser(UserName, Password);

        if (userid == -1) {
            reminderText.setText("用户不存在");
        } else if (userid == -2) {
            reminderText.setText("密码错误");
        } else {
            menuController.set_user(User.UserInfo.get(userid));
            loginVbox.getChildren().removeAll();
            reminderText.setText("登陆成功, 欢迎" + UserName);
            loginVbox.getChildren().add(reminderText);
            //等待1秒后关闭
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(event -> {
                getChildren().remove(shade);
                getChildren().remove(paper);
                getChildren().remove(loginText);
                getChildren().remove(loginVbox);
                getChildren().remove(close);
            });
            pause.play();
        }
        loginVbox.getChildren().add(2, reminderHbox);
    }
    public void handleRegister() throws FileNotFoundException {
        if(haveUser()) return;
        loginText.setText("Register");
        loginText.setFont(new Font(pixelFont.getName(), 45));
        loginText.setX(310.0);
        loginVbox.getChildren().remove(reminderHbox);
        if(!loginVbox.getChildren().contains(confirmPasswordHbox)){
            loginVbox.getChildren().add(2, confirmPasswordHbox);
            return;
        }

        UserName = usernameInput.getText();
        Password = passwordInput.getText();
        String ConfirmPassword = confirmPasswordInput.getText();

        loginVbox.getChildren().add(3, reminderHbox);

        if (SavingManager.NotValidString(UserName)) {
            reminderText.setText("不合法用户名,用户名只应包含字母、数字和_");
        } else if (SavingManager.getUser(UserName, Password) != -1) {
            reminderText.setText("用户已存在");
        } else if (SavingManager.NotValidString(Password)) {
            reminderText.setText("不合法密码,密码只应包含字母、数字和_");
        } else if (!Password.equals(ConfirmPassword)) {
            reminderText.setText("两次输入不一致");
        } else {
            SavingManager.addUser(UserName, Password);
            reminderText.setText("注册成功");
            loginVbox.getChildren().remove(confirmPasswordHbox);
        }
    }

}