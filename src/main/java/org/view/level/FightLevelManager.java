package org.view.level;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.data.mapdata;
import org.model.MapMatrix;
import org.model.Solve.Solve;
import org.model.config;
import org.view.menu.*;
import org.view.net.Client;
import org.view.net.LocalIPAddress;
import org.view.net.Server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.view.menu.MenuView.mediaPlayer;

public class FightLevelManager {
    public Pane root;
    private Stage primaryStage;
    private Scene scene;
    private MenuController controller;

    public FightLevelManager(Stage primaryStage, MenuController controller) {
        this.root = new Pane();
        this.primaryStage = primaryStage;
        this.controller = controller;
        scene = primaryStage.getScene();
        scene.setRoot(root); // 这样应该就算是一个完全新的scene了吧
    }
    public Pane levelRoot;
    public FightLevel level;
    public void start() {
        //背景颜色
        root.setStyle("-fx-background-color: #e9ddb6");
        //添加图片
        Image choice1 = new Image(getClass().getResourceAsStream("/images/choice1.png"), 130, 200, false, false);
        Image choice2 = new Image(getClass().getResourceAsStream("/images/choice2.png"), 130, 200, false, false);
        Image choice3 = new Image(getClass().getResourceAsStream("/images/choice3.png"), 130, 200, false, false);

        //添加开始游戏按钮
        Button startButton = new Button("只有一台电脑");
        startButton.setLayoutX(100);
        startButton.setLayoutY(100);
        //将按钮设置为 choice1 的图片
        startButton.setGraphic(new ImageView(choice1));
        startButton.setStyle("-fx-background-color: transparent;");
        //将文字置于图片下方
        startButton.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
        //字体
        startButton.setFont(new Font(pixelFont.getName(), 20));
        //颜色
        startButton.setTextFill(javafx.scene.paint.Color.web("#55371d"));
        //
        Button startButton2 = new Button("创建房间");
        startButton2.setLayoutX(320);
        startButton2.setLayoutY(100);
        //将按钮设置为 choice2 的图片
        startButton2.setGraphic(new ImageView(choice2));
        startButton2.setStyle("-fx-background-color: transparent;");
        //将文字置于图片下方
        startButton2.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
        //字体
        startButton2.setFont(new Font(pixelFont.getName(), 20));
        //颜色
        startButton2.setTextFill(javafx.scene.paint.Color.web("#55371d"));

        //
        Button startButton3 = new Button("加入房间");
        startButton3.setLayoutX(540);
        startButton3.setLayoutY(100);
        //将按钮设置为 choice3 的图片
        startButton3.setGraphic(new ImageView(choice3));
        startButton3.setStyle("-fx-background-color: transparent;");
        //将文字置于图片下方
        startButton3.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
        //字体
        startButton3.setFont(new Font(pixelFont.getName(), 20));
        //颜色
        startButton3.setTextFill(javafx.scene.paint.Color.web("#55371d"));
        //返回
        Button backHomeButton = new Button("返回");
        //字体
        backHomeButton.setFont(new Font(pixelFont.getName(), 20));
        //设置为透明棕色边框棕色文字
        backHomeButton.setStyle("-fx-background-color: transparent; -fx-border-color: #55371d; -fx-border-width: 2px; -fx-text-fill: #55371d;");
        backHomeButton.setLayoutX(120);
        backHomeButton.setLayoutY(400);
        backHomeButton.setOnAction(event -> {
            Home home = new Home();
            home.homeAction(root, controller, primaryStage, new MenuView(controller));
//            //停止所有 timeline
//            for(Timeline timeline : config.timelines){
//                if(timeline != null) timeline.stop();
//            }
//            //停止所有 server
//            if(FightLevelManager.server != null){
//                //如果 server 正在运行
//                if(FightLevelManager.server.socket != null && !FightLevelManager.server.socket.isClosed()) {
//                    FightLevelManager.server.send(FightLevelManager.server.socket, "B");
//                    try {
//                        FightLevelManager.server.serverSocket.close();
//                        FightLevelManager.server.socket.close();
//                    } catch (IOException e) {
////                        e.printStackTrace();
//                    }
//                }
//            }
//            //停止所有 client
//            if(FightLevelManager.client != null){
//                //如果 client 正在运行
//                if(!FightLevelManager.client.socket.isClosed()) {
//                    FightLevelManager.client.send(FightLevelManager.client.socket, "B");
//                    try {
//                        FightLevelManager.client.socket.close();
//                    } catch (IOException e) {
////                        e.printStackTrace();
//                    }
//                }
//            }
//            mediaPlayer.stop();
//            root.getChildren().clear();
//            Pane menuView = new MenuView(controller);
//            primaryStage.setScene(new Scene(menuView));
//            primaryStage.show();
        });

        //取消 startButton 对上下左右键的监听
        startButton.setFocusTraversable(false);
        startButton2.setFocusTraversable(false);
        startButton3.setFocusTraversable(false);
        backHomeButton.setFocusTraversable(false);
        startButton.setOnAction(event -> {
            startButtonAction();
            root.getChildren().remove(startButton);
            root.getChildren().remove(startButton2);
            root.getChildren().remove(startButton3);
            root.getChildren().remove(backHomeButton);
        });
        startButton2.setOnAction(event -> {
            startButton2Action();
            root.getChildren().remove(startButton);
            root.getChildren().remove(startButton2);
            root.getChildren().remove(startButton3);
            root.getChildren().remove(backHomeButton);
        });
        startButton3.setOnAction(event -> {
            startButton3Action();
            root.getChildren().remove(startButton);
            root.getChildren().remove(startButton2);
            root.getChildren().remove(startButton3);
            root.getChildren().remove(backHomeButton);
        });
        root.getChildren().add(startButton);
        root.getChildren().add(startButton2);
        root.getChildren().add(startButton3);
        root.getChildren().add(backHomeButton);
    }

    void startButtonAction(){
        type = 1;
        config.tile_size = 48;
//        if(levelRoot == null)
        levelRoot = new Pane();
//        if(level == null)
        level = new FightLevel(levelRoot, 1, primaryStage, null, true);

        int id;
        while(true){
            //在 0-4 之间随机选择一个地图
            id = (int) (Math.random() * mapdata.maps.length);
            double startTime = System.currentTimeMillis();
            Solve solve = new Solve(new MapMatrix(mapdata.maps[id]));
            solve.aStarSearch();
            double solveTime = System.currentTimeMillis() - startTime;
            if(solveTime < 50) break;
        }

        level.setId(id);
        for(Timeline timeline : config.timelines){
            if(timeline != null) timeline.stop();
        }
        level.init();
        root.getChildren().add(levelRoot);
        inLevel(level);
    }
    Font pixelFont = Font.loadFont(getClass().getResource("/font/pixel.ttf").toExternalForm(), 30);
    public Text waitingText;
    public int FightLevelID;
    public static Server server;
    public static Client client;
    int type = 0;
    public Button backButton0;
    void startButton2Action(){ //房主
        type = 2;
        config.tile_size = 48;
        while(true){
            //在 0-4 之间随机选择一个地图
            FightLevelID = (int) (Math.random() * mapdata.maps.length);
            double startTime = System.currentTimeMillis();
            Solve solve = new Solve(new MapMatrix(mapdata.maps[FightLevelID]));
            solve.aStarSearch();
            double solveTime = System.currentTimeMillis() - startTime;
            if(solveTime < 50) break;
        }

        //显示文字
        waitingText = new Text(230, 280, LocalIPAddress.getLocalIP());
        waitingText.setFont(new Font(pixelFont.getName(), 45));
        waitingText.setFill(javafx.scene.paint.Color.web("#55371d"));
        root.getChildren().add(waitingText);
        //添加返回按钮
        backButton0 = new Button("返回");
        backButton0.setFont(new Font(pixelFont.getName(), 15));
        backButton0.setStyle("-fx-background-color: transparent; -fx-border-color: #55371d; -fx-border-width: 2px; -fx-text-fill: #55371d;");
        backButton0.setLayoutX(230);
        backButton0.setLayoutY(330);
        backButton0.setFocusTraversable(false);
        backButton0.setOnAction(event -> {
            root.getChildren().remove(waitingText);
            root.getChildren().remove(backButton0);
                try {
                    server.serverSocket.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            start();
        });
        root.getChildren().add(backButton0);

        new Thread(() -> {
            server = new Server(this);
            server.start(8888, 2, LocalIPAddress.getLocalIP());
            server.send(server.socket, IDtoString("M", FightLevelID));
        }).start();
//        Platform.runLater(() -> {
//            root.getChildren().add(levelRoot);
////            inLevel(level);
//        });
    }
    String IDtoString(String s, int id){
        return s + Integer.toString(id);
    }
    void startButton3Action(){
        type = 3;
        config.tile_size = 48;
//        //显示文字
//        waitingText = new Text(230, 280, "正在寻找房间……");
//        waitingText.setFont(new Font(pixelFont.getName(), 45));
//        waitingText.setFill(javafx.scene.paint.Color.web("#55371d"));
//        root.getChildren().add(waitingText);
        //输入框
        Text text = new Text(230, 230, "请输入房主的 IP 地址：");
        text.setFont(new Font(pixelFont.getName(), 30));
        text.setFill(javafx.scene.paint.Color.web("#55371d"));
        root.getChildren().add(text);
        //输入框
        javafx.scene.control.TextField textField = new javafx.scene.control.TextField();
        textField.setStyle("-fx-background-color: transparent; -fx-border-color: #55371d; -fx-border-width: 2px;");
        textField.setLayoutX(230);
        textField.setLayoutY(260);
        textField.setPrefWidth(200);
        textField.setPrefHeight(30);
        textField.setFocusTraversable(false);
        root.getChildren().add(textField);
        //取消输入框对上下左右键的监听
        //确认按钮
        Button confirmButton = new Button("确认");
        //字体
        confirmButton.setFont(new Font(pixelFont.getName(), 15));
        //设置为透明棕色边框棕色文字
        confirmButton.setStyle("-fx-background-color: transparent; -fx-border-color: #55371d; -fx-border-width: 2px; -fx-text-fill: #55371d;");
        confirmButton.setLayoutX(230);
        confirmButton.setLayoutY(320);
        confirmButton.setFocusTraversable(false);
        root.getChildren().add(confirmButton);
        //返回按钮
        backButton0 = new Button("返回");
        //字体
        backButton0.setFont(new Font(pixelFont.getName(), 15));
        //设置为透明棕色边框棕色文字
        backButton0.setStyle("-fx-background-color: transparent; -fx-border-color: #55371d; -fx-border-width: 2px; -fx-text-fill: #55371d;");
        backButton0.setLayoutX(300);
        backButton0.setLayoutY(320);
        backButton0.setFocusTraversable(false);
        backButton0.setOnAction(event -> {
            //取消 client 通信
            if(client != null){
                client.connected = true;
            }
            root.getChildren().remove(text);
            root.getChildren().remove(textField);
            root.getChildren().remove(confirmButton);
            root.getChildren().remove(backButton0);
            start();
        });
        root.getChildren().add(backButton0);

        confirmButton.setOnAction(event -> {
            new Thread(() -> {
                client = new Client(this);
                client.start(textField.getText(), 8888);
            }).start();
            root.getChildren().remove(text);
            root.getChildren().remove(textField);
            root.getChildren().remove(confirmButton);
        });

        FightLevelID = -1;
    }
    public void button2LoadLevel(Socket socket){
//        System.out.println("房主真 load 了");
//        if(levelRoot == null)
        levelRoot = new Pane();
//        if(level == null)
        level = new FightLevel(levelRoot, FightLevelID, primaryStage, null, true);
        root.getChildren().add(levelRoot);
        inLevel2(level, socket);
    }
    public void button3LoadLevel(Socket socket){
//        if(levelRoot == null)
        levelRoot = new Pane();
//        if(level == null)
        level = new FightLevel(levelRoot, FightLevelID, primaryStage, null, true);
        root.getChildren().add(levelRoot);
        inLevel3(level, socket);
    }

    public Button themeButton;
    public void createThemeButton(){
        Theme theme = new Theme();
        themeButton = theme.createButton(root);
    }
    public Button settingsButton;
    public void createSettingsButton(){
        Settings settings = new Settings();
        settingsButton = settings.createButton(root);
    }
    public Button homeButton;
    public void createHomeButton(){
        Home home = new Home();
        homeButton = home.createButton(root, primaryStage, controller);
    }
    public int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
    private void inLevel2(FightLevel level, Socket socket) {
        createThemeButton();
        createSettingsButton();
        createHomeButton();
        root.getChildren().add(themeButton);
        root.getChildren().add(settingsButton);
        root.getChildren().add(homeButton);

        // 添加键盘监听功能
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
//             System.out.println(code);
            level.player1.set_velocity(0, 0);
            level.player2.set_velocity(0, 0);
            dx1 = 0; dy1 = 0; dx2 = 0; dy2 = 0;
            if (code == KeyCode.W || code == KeyCode.UP) {
                dy1 = -1;
                level.player1.setOrientation(1);
                server.send(socket, "W");
            } else if (code == KeyCode.S || code == KeyCode.DOWN) {
                dy1 = 1;
                level.player1.setOrientation(2);
                server.send(socket, "S");
            } else if (code == KeyCode.A || code == KeyCode.LEFT) {
                dx1 = -1;
                level.player1.setOrientation(3);
                server.send(socket, "A");
            } else if (code == KeyCode.D || code == KeyCode.RIGHT) {
                dx1 = 1;
                level.player1.setOrientation(4);
                server.send(socket, "D");
            } else return;
            keyPressedEvent(dx1, dy1, dx2, dy2, level);
        });
        checkSize();
    }
    public void inLevel3(FightLevel level, Socket socket) {
        createSettingsButton();
        createHomeButton();
        createThemeButton();
        root.getChildren().add(settingsButton);
        root.getChildren().add(homeButton);
        root.getChildren().add(themeButton);
        // 添加键盘监听功能
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
//             System.out.println(code);
            level.player1.set_velocity(0, 0);
            level.player2.set_velocity(0, 0);
            dx1 = 0; dy1 = 0; dx2 = 0; dy2 = 0;
            if (code == KeyCode.W || code == KeyCode.UP) {
                dy2 = -1;
                level.player2.setOrientation(1);
                client.send(socket, "W");
            } else if (code == KeyCode.S || code == KeyCode.DOWN) {
                dy2 = 1;
                level.player2.setOrientation(2);
                client.send(socket, "S");
            } else if (code == KeyCode.A || code == KeyCode.LEFT) {
                dx2 = -1;
                level.player2.setOrientation(3);
                client.send(socket, "A");
            } else if (code == KeyCode.D || code == KeyCode.RIGHT) {
                dx2 = 1;
                level.player2.setOrientation(4);
                client.send(socket, "D");
            } else return;
            keyPressedEvent(dx1, dy1, dx2, dy2, level);
        });
        checkSize();
    }
    public void keyCodeEvent(KeyCode code){
        level.player1.set_velocity(0, 0);
        level.player2.set_velocity(0, 0);
        dx1 = 0; dy1 = 0; dx2 = 0; dy2 = 0;
        if (code == KeyCode.W) {
            dy1 = -1;
            level.player1.setOrientation(1);
        } else if (code == KeyCode.S) {
            dy1 = 1;
            level.player1.setOrientation(2);
        } else if (code == KeyCode.A) {
            dx1 = -1;
            level.player1.setOrientation(3);
        } else if (code == KeyCode.D) {
            dx1 = 1;
            level.player1.setOrientation(4);
        } else if (code == KeyCode.UP) {
            dy2 = -1;
            level.player2.setOrientation(1);
        } else if (code == KeyCode.DOWN) {
            dy2 = 1;
            level.player2.setOrientation(2);
        } else if (code == KeyCode.LEFT) {
            dx2 = -1;
            level.player2.setOrientation(3);
        } else if (code == KeyCode.RIGHT) {
            dx2 = 1;
            level.player2.setOrientation(4);
        } else return;
        keyPressedEvent(dx1, dy1, dx2, dy2, level);
    }
    private void inLevel(FightLevel level) {
        createSettingsButton();
        createHomeButton();
        createThemeButton();
        root.getChildren().add(themeButton);
        root.getChildren().add(settingsButton);
        root.getChildren().add(homeButton);
        // 添加键盘监听功能
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
//             System.out.println(code);
            level.player1.set_velocity(0, 0);
            level.player2.set_velocity(0, 0);
            dx1 = 0; dy1 = 0; dx2 = 0; dy2 = 0;
            if (code == KeyCode.W) {
                dy1 = -1;
                level.player1.setOrientation(1);
            } else if (code == KeyCode.S) {
                dy1 = 1;
                level.player1.setOrientation(2);
            } else if (code == KeyCode.A) {
                dx1 = -1;
                level.player1.setOrientation(3);
            } else if (code == KeyCode.D) {
                dx1 = 1;
                level.player1.setOrientation(4);
            } else if (code == KeyCode.UP) {
                dy2 = -1;
                level.player2.setOrientation(1);
            } else if (code == KeyCode.DOWN) {
                dy2 = 1;
                level.player2.setOrientation(2);
            } else if (code == KeyCode.LEFT) {
                dx2 = -1;
                level.player2.setOrientation(3);
            } else if (code == KeyCode.RIGHT) {
                dx2 = 1;
                level.player2.setOrientation(4);
            } else return;
            keyPressedEvent(dx1, dy1, dx2, dy2, level);
        });
        checkSize();
    }
    public Button restartButton, backButton;
    public void keyPressedEvent(int dx1, int dy1, int dx2, int dy2, FightLevel level) {
        level.player1.set_velocity(dx1, dy1);
        level.player2.set_velocity(dx2, dy2);
//        if(!level.player1.is_moving){
            level.player1.move(level.getMap(), level.boxes, level);
            level.player1.setImageTowards(level.player1.getOrientation());
//        }
//        if(!level.player2.is_moving){
            level.player2.move(level.getMap(), level.boxes, level);
            level.player2.setImageTowards(level.player2.getOrientation());
//        }
        level.drawMap();
        int whoWin = level.oneIsWin();
        if (whoWin != 0 || level.checkDraw()) {
            level.stopTimelines();
            //移除监听器
            scene.setOnKeyPressed(null);
            //让胜利的文字显示在屏幕中间
            if(whoWin != 0) showWinText("Player" + whoWin + " Win!");
            else showWinText("Draw!");
            if(type != 3) {
                //再来一局按钮
                restartButton = new Button("再来一局");
                restartButton.setLayoutX(primaryStage.getWidth() / 2 - 65);
                restartButton.setLayoutY(primaryStage.getHeight() / 2 + 50);
                //取消 restartButton 对上下左右键的监听
                restartButton.setFocusTraversable(false);
                //设置为透明白色边框白色文字
                restartButton.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 2px; -fx-text-fill: white;");
                restartButton.setFont(new Font(pixelFont.getName(), 25));
                //返回按钮
                backButton = new Button("返回");
                backButton.setLayoutX(primaryStage.getWidth() / 2 - 45);
                backButton.setLayoutY(primaryStage.getHeight() / 2 + 120);
                //取消 backButton 对上下左右键的监听
                backButton.setFocusTraversable(false);
                //设置为透明白色边框白色文字
                backButton.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 2px; -fx-text-fill: white;");
                backButton.setFont(new Font(pixelFont.getName(), 25));
                backButton.setOnAction(event -> {
                    root.getChildren().remove(vbox);
                    root.getChildren().remove(restartButton);
                    root.getChildren().remove(backButton);
                    root.getChildren().remove(backButton0);
                    root.getChildren().remove(levelRoot);
                    root.getChildren().remove(waitingText);
                    root.getChildren().remove(settingsButton);
                    root.getChildren().remove(themeButton);
                    root.getChildren().remove(homeButton);
                    level.root.getChildren().clear();
                    if(type == 2){
                        server.send(server.socket, "B");
                        //结束 server
                        try {
                            server.socket.close();
                            server.serverSocket.close();
                        } catch (IOException e) {
//                            e.printStackTrace();
                        }
                    }
                    start();
                });
                restartButton.setOnAction(event -> {
                    root.getChildren().remove(vbox);
                    root.getChildren().remove(restartButton);
                    root.getChildren().remove(backButton);
                    root.getChildren().remove(backButton0);
                    root.getChildren().remove(settingsButton);
                    root.getChildren().remove(themeButton);
                    root.getChildren().remove(homeButton);
                    if(type == 2) inLevel2(level, server.socket);
                    if(type == 1) inLevel(level);
                    level.setId((int) (Math.random() * mapdata.maps.length));
                    level.init();
                    if(type == 2){
                        server.send(server.socket, IDtoString("R", level.getId()));
                    }
                });
                root.getChildren().add(restartButton);
                root.getChildren().add(backButton);
            }
        }
    }
//    Font pixelFont = Font.loadFont(getClass().getResource("/font/pixel.ttf").toExternalForm(), 90);
    public VBox vbox;
    Line topLine, bottomLine;
    Label label;
    public void showWinText(String text) {

        // 两条线
        topLine = new Line(0, 0, primaryStage.getWidth(), 0);
        topLine.setStroke(Color.WHITE);
        topLine.setStrokeWidth(config.Win_Rect_Stroke);

        bottomLine = new Line(0, config.Win_Rect_Height, primaryStage.getWidth(), config.Win_Rect_Height);
        bottomLine.setStroke(Color.WHITE);
        bottomLine.setStrokeWidth(config.Win_Rect_Stroke);

        label = new Label(text);
        label.setFont(pixelFont);
        label.setTextFill(Color.WHITE);

        // 组合
        vbox = new VBox(topLine, label, bottomLine);
        vbox.setSpacing(10);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        vbox.setPadding(new Insets(primaryStage.getHeight() / 2 - 110,0,0,0));

        root.getChildren().add(vbox);

        // 从左边出现
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(config.win_in_duration), vbox);
        translateTransition.setFromX(-primaryStage.getWidth());
        translateTransition.setToX(0);
        translateTransition.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1));
    }

    //检测屏幕大小变化
    public void checkSize() {
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
//            primaryStage.setWidth((double) newValue);
            level.getCanvas().setWidth(newValue.doubleValue());
            level.drawMap();
//            level.setAnchor_posx(level.getanchor_posx() + (newValue.doubleValue() - oldValue.doubleValue()) / 2);
            //玩家

            if(vbox != null) vbox.setPadding(new Insets(primaryStage.getHeight() / 2 - 110,0,0,0));
            if(topLine != null) topLine.setEndX(primaryStage.getWidth());
            if(bottomLine != null) bottomLine.setEndX(primaryStage.getWidth());
            if(restartButton != null) restartButton.setLayoutX(primaryStage.getWidth() / 2 - 65);
            if(backButton != null) backButton.setLayoutX(primaryStage.getWidth() / 2 - 45);
            //设置和主界面图标
            if(settingsButton != null) settingsButton.setLayoutX(primaryStage.getWidth() - 80);
            if(homeButton != null) homeButton.setLayoutX(primaryStage.getWidth() - 150);
            if(themeButton != null) themeButton.setLayoutX(primaryStage.getWidth() - 220);
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
//            primaryStage.setHeight((double) newValue);
            level.getCanvas().setHeight(newValue.doubleValue());
            level.drawMap();

            if(restartButton != null) restartButton.setLayoutY(primaryStage.getHeight() / 2 + 50);
            if(backButton != null) backButton.setLayoutY(primaryStage.getHeight() / 2 + 120);
        });
    }

}