package org.view.level;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.data.mapdata;
import org.model.SavingManager;
import org.model.User;
import org.model.config;
import org.view.LevelSelect.MapNode;
import org.view.LevelSelect.SelectMap;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.view.menu.Home;
//import org.view.menu.MenuController;
import org.view.menu.MenuController;
import org.view.menu.Settings;
import org.view.menu.Theme;

import static org.data.mapdata.maps;

public class LevelManager {
    private int currentLevel;
    private int totalLevel;
    private Pane root;
    private Scene scene;
    private Stage primaryStage; // 直接作为属性 不然函数里有一坨（
    private SelectMap level_menu; // 选关界面
    private int move_count;
    private Level level;
    MenuController controller;

    private User user = null; // 正在游玩的user，用于存档

    public LevelManager(Stage primaryStage, MenuController controller) {
        this.root = new Pane(); // 直接用新的Pane 除了stage其他全部新建
        this.controller = controller;
        this.currentLevel = 0;
        this.totalLevel = maps.length;
        this.primaryStage = primaryStage; // 主舞台

//        Pane level_menu_root = new Pane();
//        this.level_menu = new SelectMap(primaryStage, level_menu_root); // 选关界面
//        root.getChildren().add(level_menu_root);

        MapNode.levelManager = this;
    }

    public void loadLevel(int id, int[][][] maps){
        root.getChildren().clear();
        root.setLayoutX(0); root.setLayoutY(0);
        currentLevel = id;
        Pane rootLevel = new Pane();
        root.getChildren().add(rootLevel);
        level = new NormalLevel(rootLevel, currentLevel, maps, primaryStage, user);
        scene = primaryStage.getScene();
        scene.setRoot(root); // 这样应该就算是一个完全新的scene了吧

        InLevel(id);
    }
    public void loadLevel(int id, int step, int[][] mapmatrix) {
        root.getChildren().clear();
        root.setLayoutX(0); root.setLayoutY(0);
        currentLevel = id;
        Pane rootLevel = new Pane();
        root.getChildren().add(rootLevel);
        level = new NormalLevel(rootLevel, mapmatrix, primaryStage, currentLevel, user);
        level.step = step;
        level.stepText.setText("移动步数: " + step);
        scene = primaryStage.getScene();
        scene.setRoot(root); // 这样应该就算是一个完全新的scene了吧

//        System.out.println(step);
        InLevel(id);
    }

    ImageView item_hint, item_plus, item_withdraw;
    Text item_hintText, item_plusText, item_withdrawText;
    boolean isDraggingItem = false;
    private void addItem_hint(Pane root){
        item_hintText = new Text("x" + config.item_hintNumber);
        item_hintText.setFont(new Font(pixelFont.getName(), 23));
        item_hintText.setFill(javafx.scene.paint.Color.web("#55371d"));
        item_hintText.setLayoutX(70);
        item_hintText.setLayoutY(primaryStage.getHeight() - 63);
        root.getChildren().add(item_hintText);
        item_hint = new ImageView(new Image(getClass().getResourceAsStream("/images/hint.png"), 40, 40, false, false));
        item_hint.setLayoutX(45);
        item_hint.setLayoutY(primaryStage.getHeight() - 135);
        item_hint.setOnMouseDragged(event -> {
            if(IAmInLevel) {
                item_hint.setX(event.getX() - 20);
                item_hint.setY(event.getY() - 20);
            }
        });
        item_hint.setOnMousePressed(event -> {
            if(IAmInLevel)
                isDraggingItem = true;
        });
        item_hint.setOnMouseReleased(event -> {
            if(item_hint.getY() < -50){
                Hint hint = new Hint(level);
                config.this_is_hint = true;
                char res = hint.autoMoveOnce();
                config.this_is_hint = false;
                if(res == 'n'){
                    level.isEnd = true;
                    Win lose_anim = new Win(primaryStage, root);
                    lose_anim.lose();
                }
                --config.item_hintNumber;
                user.setItem_hintNumber(user.getItem_hintNumber() - 1);
                try{
                    SavingManager.save();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                item_hintText.setText("x" + config.item_hintNumber);
                if(config.item_hintNumber == 0) root.getChildren().remove(item_hint);
            }
            isDraggingItem = false;
            //回到原来的位置
            item_hint.setX(0);
            item_hint.setY(0);
        });
        if(config.item_hintNumber > 0) root.getChildren().add(item_hint);
    }

    private void addItem_plus(Pane root){
        item_plusText = new Text("x" + config.item_plusNumber);
        item_plusText.setFont(new Font(pixelFont.getName(), 23));
        item_plusText.setFill(javafx.scene.paint.Color.web("#55371d"));
        item_plusText.setLayoutX(130);
        item_plusText.setLayoutY(primaryStage.getHeight() - 63);
        root.getChildren().add(item_plusText);

        item_plus = new ImageView(new Image(getClass().getResourceAsStream("/images/plus.png"), 40, 40, false, false));
        item_plus.setLayoutX(105);
        item_plus.setLayoutY(primaryStage.getHeight() - 135);
        item_plus.setOnMouseDragged(event -> {
            if(IAmInLevel) {
                item_plus.setX(event.getX() - 20);
                item_plus.setY(event.getY() - 20);
            }
        });
        item_plus.setOnMousePressed(event -> {
            if(IAmInLevel)
                isDraggingItem = true;
        });
        item_plus.setOnMouseReleased(event -> {
            if(item_plus.getY() < -50){
                level.setStepLimit(level.getStepLimit() + 5);
                --config.item_plusNumber;
                user.setItem_plusNumber(user.getItem_plusNumber() - 1);
                try{
                    SavingManager.save();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                item_plusText.setText("x" + config.item_plusNumber);
                if(config.item_plusNumber == 0) root.getChildren().remove(item_plus);
            }
            isDraggingItem = false;
            //回到原来的位置
            item_plus.setX(0);
            item_plus.setY(0);
        });
        if(config.item_plusNumber > 0) root.getChildren().add(item_plus);
    }

    private void addItem_withdraw(Pane root){
        item_withdrawText = new Text("x" + config.item_withdrawNumber);
        item_withdrawText.setFont(new Font(pixelFont.getName(), 23));
        item_withdrawText.setFill(javafx.scene.paint.Color.web("#55371d"));
        item_withdrawText.setLayoutX(195);
        item_withdrawText.setLayoutY(primaryStage.getHeight() - 63);
        root.getChildren().add(item_withdrawText);

        item_withdraw = new ImageView(new Image(getClass().getResourceAsStream("/images/withdraw.png"), 40, 40, false, false));
        item_withdraw.setLayoutX(165);
        item_withdraw.setLayoutY(primaryStage.getHeight() - 135);
        item_withdraw.setOnMouseDragged(event -> {
            if(IAmInLevel) {
                item_withdraw.setX(event.getX() - 20);
                item_withdraw.setY(event.getY() - 20);
            }
        });
        item_withdraw.setOnMousePressed(event -> {
            if(IAmInLevel)
                 isDraggingItem = true;
        });
        item_withdraw.setOnMouseReleased(event -> {
            if(item_withdraw.getY() < -50){
//                撤销操作
                if(level.player.move_back(level.getMap(), level.boxes, level)){
                    level.addStep(-1);
                    --config.item_withdrawNumber;
                    user.setItem_withdrawNumber(user.getItem_withdrawNumber() - 1);
                    try{
                        SavingManager.save();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    item_withdrawText.setText("x" + config.item_withdrawNumber);
                    if(config.item_withdrawNumber == 0) root.getChildren().remove(item_withdraw);
                }
            }
            isDraggingItem = false;
            //回到原来的位置
            item_withdraw.setX(0);
            item_withdraw.setY(0);
        });
        if(config.item_withdrawNumber > 0) root.getChildren().add(item_withdraw);
    }

    ImageView itemsImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/items.png"), 190, 70, false, false));
    private void InLevelMenu(){
        IAmInLevel = false;
        createSettingsButton();
        createHomeButton();
        createThemeButton();
        drawSettingsButton(root);
        drawHomeButton(root);
        drawThemeButton(root);

        // 监听大小改变
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            setSettingsButton();
            setHomeButton();
            setThemeButton();
        });


        //道具栏
        itemsImageView.setLayoutX(30);
        itemsImageView.setLayoutY(primaryStage.getHeight() - 150);
        root.getChildren().add(itemsImageView);
        addItem_hint(root);
        addItem_plus(root);
        addItem_withdraw(root);

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            itemsImageView.setLayoutY(newValue.doubleValue() - 112);
            item_hint.setLayoutY(newValue.doubleValue() - 97);
            item_plus.setLayoutY(newValue.doubleValue() - 97);
            item_withdraw.setLayoutY(newValue.doubleValue() - 97);
            item_hintText.setLayoutY(newValue.doubleValue() - 63);
            item_plusText.setLayoutY(newValue.doubleValue() - 63);
            item_withdrawText.setLayoutY(newValue.doubleValue() - 63);
        });
    }
    boolean IAmInLevel = false;
    private void InLevel(int id) {
        IAmInLevel = true;
        createDirectionButtons();
        createSettingsButton();
        createHomeButton();
        createThemeButton();
        drawDirectionButtons(root);
        drawSettingsButton(root);
        drawHomeButton(root);
        drawThemeButton(root);

        //道具栏
        itemsImageView.setLayoutX(30);
        itemsImageView.setLayoutY(primaryStage.getHeight() - 150);
        root.getChildren().add(itemsImageView);
        addItem_hint(root);
        addItem_plus(root);
        addItem_withdraw(root);

        // 添加键盘监听功能
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
//             System.out.println(code);
            level.player.set_velocity(0, 0);
            int dx = 0, dy = 0;
            if(code == KeyCode.UP || code == KeyCode.W) {dy = -1; level.player.setOrientation(1);}
            else if(code == KeyCode.DOWN || code == KeyCode.S && !event.isControlDown()) {dy = 1; level.player.setOrientation(2);}
            else if(code == KeyCode.LEFT || code == KeyCode.A) { dx = -1; level.player.setOrientation(3);}
            else if(code == KeyCode.RIGHT || code == KeyCode.D) { dx = 1; level.player.setOrientation(4);}
            else if(code == KeyCode.R){
                level.stopTimelines();
                user.setMoveCount(0);
                level.init();
                // System.out.println("reset");
            }
            else if(code == KeyCode.ESCAPE){
                //回到关卡选择界面并移除监听和动画
                scene.setOnKeyPressed(null);
                scene.setOnMousePressed(null);
                scene.setOnMouseDragged(null);
                level.stopTimelines();
                showLevelMenu();
            }
            else if(event.isControlDown() && code == KeyCode.S){ //同时按下control s时保存
                try {
                    save("保存成功");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if(code == KeyCode.U) { // 别忘删了
                config.this_is_hint = true;
                Hint hint = new Hint(level);
                hint.autoMove();
                config.this_is_hint = false;
            } else if(code == KeyCode.J) {
//                Hint hint = new Hint(level);
//                hint.autoMoveOnce();
            }
            else return;
            keyPressedEvent(dx, dy, id);
        });


        // 根据鼠标拖动改变anchor_posx anchor_posy
        // 鼠标是否在拖动根据鼠标拖动改变anchor_posx
        AtomicBoolean isDragging = new AtomicBoolean(false);
        AtomicReference<Double> del_posx = new AtomicReference<>((double) 0);
        AtomicReference<Double> del_posy = new AtomicReference<>((double) 0);

        // 添加鼠标按下事件监听器
        scene.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                del_posx.set(level.getanchor_posx() - event.getSceneX());
                del_posy.set(level.getanchor_posy() - event.getSceneY());
            }
        });

        scene.setOnMouseDragged(event -> {
            if(!isDraggingItem){
                level.player.stopCameraTimeline();
                level.setAnchor_posx(event.getSceneX() + del_posx.get());
                level.setAnchor_posy(event.getSceneY() + del_posy.get());
                level.updateAllRadiatingEffect();
                level.drawMap();
            }
        });

        // 监听大小改变
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            // level.setAnchor_posx(level.getAnchor_posx() + (newValue.doubleValue() - oldValue.doubleValue()) / 2);
            level.getCanvas().setWidth(newValue.doubleValue());
//            level.settingsButton.setLayoutX(newValue.doubleValue() - 80);
            level.drawMap();
            setDirectionButtons();
            setSettingsButton();
            setHomeButton();
            setThemeButton();
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            // level.setAnchor_posy(level.getAnchor_posy() + (newValue.doubleValue() - oldValue.doubleValue()) / 2);
            level.getCanvas().setHeight(newValue.doubleValue());
            level.drawMap();
            setDirectionButtons();
            itemsImageView.setLayoutY(newValue.doubleValue() - 112);
            item_hint.setLayoutY(newValue.doubleValue() - 97);
            item_plus.setLayoutY(newValue.doubleValue() - 97);
            item_withdraw.setLayoutY(newValue.doubleValue() - 97);
            item_hintText.setLayoutY(newValue.doubleValue() - 63);
            item_plusText.setLayoutY(newValue.doubleValue() - 63);
            item_withdrawText.setLayoutY(newValue.doubleValue() - 63);
        });
    }

    public void keyPressedEvent(int dx, int dy, int id){
        if(level.isEnd) return;
        if(level.isWin()) return; // 目前来看表现正常
        level.player.set_velocity(dx, dy);
        if(!level.player.is_moving){
            if(level.player.move(level.getMap(), level.boxes, level)){
                if(level.getStep() >= level.getStepLimit()){
                    if(!level.isEnd){
                        Win out_anim = new Win(primaryStage, root);
                        out_anim.outOfLimit();
                    }
                    level.isEnd = true;
                    return;
                }
                user.addMoveCount();
                level.addStep(1);
            }
            level.player.setImageTowards(level.player.getOrientation());
        }

        level.drawMap();

        if(level.isWin()){
            Win Win_anim = new Win(primaryStage, root);

            Win_anim.win();

            Win_anim.sequentialTransition.setOnFinished(ev -> {
                level.stopTimelines();
                user.setLevelAt(++currentLevel);
                user.setLevelAtStep(level.getStep());
                user.setMaxLevel(Math.max(currentLevel, user.getMaxLevel()));
                user.setMoveCount(0);
                if(currentLevel == maps.length) currentLevel = 0;

                //loadLevel(id + 1, mapdata.maps);
                showLevelMenu();

                try {
                    save("自动保存成功");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if(level.solve_next_move() == 'N'){
            level.isEnd = true;
            Win lose_anim = new Win(primaryStage, root);
            lose_anim.lose();
        }
    }

    Button upButton, downButton, leftButton, rightButton;
    static public VBox vbox;
    static public HBox hbox;
    static public Text explainText = new Text("R：重置 ESC：退出 Ctrl+S：存档");
    public static void drawDirectionButtons(Pane root){
//        root.getChildren().add(vbox);
        root.getChildren().add(hbox);
        root.getChildren().add(explainText);
    }
    static Button homeButton;
    static Button settingsButton;
    static Button themeButton;
    public static void drawSettingsButton(Pane root){
        root.getChildren().add(settingsButton);
    }
    public static void drawThemeButton(Pane root){ root.getChildren().add(themeButton);}
    public void setDirectionButtons(){
        hbox.setLayoutX(primaryStage.getWidth() - 200);
        hbox.setLayoutY(primaryStage.getHeight() - 200);
        explainText.setLayoutX(primaryStage.getWidth() - 230);
        explainText.setLayoutY(primaryStage.getHeight() - 70);
    }
    public void createSettingsButton(){
        Settings settings = new Settings();
        settingsButton = settings.createButton(root);
    }
    public void setSettingsButton(){
        settingsButton.setLayoutX(primaryStage.getWidth() - 80);
    }
    public void createThemeButton(){
        Theme theme = new Theme();
        themeButton = theme.createButton(root);
    }
    public void setThemeButton(){
        themeButton.setLayoutX(primaryStage.getWidth() - 220);
    }

    public void createHomeButton(){
        Home home = new Home();
        homeButton = home.createButton(root, primaryStage, controller);
    }
    public void setHomeButton(){
        homeButton.setLayoutX(primaryStage.getWidth() - 150);
    }
    public void drawHomeButton(Pane root){
        root.getChildren().add(homeButton);
    }
    Font pixelFont = Font.loadFont(getClass().getResource("/font/pixel.ttf").toExternalForm(), 15);

    public void createDirectionButtons() {

        explainText.setFont(pixelFont);
        explainText.setFill(javafx.scene.paint.Color.web("#55371d"));

        Image upImage = new Image(getClass().getResourceAsStream("/images/direction/up.png"), config.button_size, config.button_size, false, false);
        Image downImage = new Image(getClass().getResourceAsStream("/images/direction/down.png"), config.button_size, config.button_size, false, false);
        Image leftImage = new Image(getClass().getResourceAsStream("/images/direction/left.png"), config.button_size, config.button_size, false, false);
        Image rightImage = new Image(getClass().getResourceAsStream("/images/direction/right.png"), config.button_size, config.button_size, false, false);

        upButton = new Button();
        downButton = new Button();
        leftButton = new Button();
        rightButton = new Button();

        upButton.setGraphic(new ImageView(upImage));
        downButton.setGraphic(new ImageView(downImage));
        leftButton.setGraphic(new ImageView(leftImage));
        rightButton.setGraphic(new ImageView(rightImage));

        // Remove button background, border, and text
        upButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        downButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        leftButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        rightButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        upButton.setFocusTraversable(false);
        downButton.setFocusTraversable(false);
        leftButton.setFocusTraversable(false);
        rightButton.setFocusTraversable(false);

        upButton.setOnAction(event -> simulateKeyPress(KeyCode.UP));
        upButton.setOnMouseEntered(event -> upButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/direction/up_clicked.png"), config.button_size, config.button_size, false, false))));
        upButton.setOnMouseExited(event -> upButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/direction/up.png"), config.button_size, config.button_size, false, false))));

        downButton.setOnAction(event -> simulateKeyPress(KeyCode.DOWN));
        downButton.setOnMouseEntered(event -> downButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/direction/down_clicked.png"), config.button_size, config.button_size, false, false))));
        downButton.setOnMouseExited(event -> downButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/direction/down.png"), config.button_size, config.button_size, false, false))));

        leftButton.setOnAction(event -> simulateKeyPress(KeyCode.LEFT));
        leftButton.setOnMouseEntered(event -> leftButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/direction/left_clicked.png"), config.button_size, config.button_size, false, false))));
        leftButton.setOnMouseExited(event -> leftButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/direction/left.png"), config.button_size, config.button_size, false, false))));

        rightButton.setOnAction(event -> simulateKeyPress(KeyCode.RIGHT));
        rightButton.setOnMouseEntered(event -> rightButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/direction/right_clicked.png"), config.button_size, config.button_size, false, false))));
        rightButton.setOnMouseExited(event -> rightButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/direction/right.png"), config.button_size, config.button_size, false, false))));

        vbox = new VBox(upButton, downButton);
        hbox = new HBox(leftButton, vbox, rightButton);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        setDirectionButtons();
        //缩小按钮间距
        upButton.setMinSize(config.button_size, config.button_size);
        downButton.setMinSize(config.button_size, config.button_size);
        leftButton.setMinSize(config.button_size, config.button_size);
        rightButton.setMinSize(config.button_size, config.button_size);
        upButton.setMaxSize(config.button_size, config.button_size);
        downButton.setMaxSize(config.button_size, config.button_size);
        leftButton.setMaxSize(config.button_size, config.button_size);
        rightButton.setMaxSize(config.button_size, config.button_size);

        //缩小按钮的间隔
        vbox.setSpacing(config.button_gap);
        hbox.setSpacing(config.button_gap);

    }
    private void simulateKeyPress(KeyCode keyCode) {
        int dx = 0, dy = 0;

        switch (keyCode) {
            case UP:
                dy = -1; level.player.setOrientation(1);
                break;
            case DOWN:
                dy = 1; level.player.setOrientation(2);
                break;
            case LEFT:
                dx = -1; level.player.setOrientation(3);
                break;
            case RIGHT:
                dx = 1; level.player.setOrientation(4);
                break;
        }
        keyPressedEvent(dx, dy, currentLevel);
    }
    public void start() {
        if(user.getLevelAtStep() > 0) {
            Alert alert  = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("提示");
            alert.setHeaderText("检测到上次游戏未结束，是否继续？");
            alert.setContentText("选择\"是\"继续游戏，选择\"否\"从选关界面开始");
            alert.initOwner(primaryStage); // 设置提示窗口的所有者为主窗口
            alert.initModality(Modality.APPLICATION_MODAL); // 设置提示窗口为模态窗口

            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
            if(result == ButtonType.OK) {
                loadLevel(user.getLevelAt(), user.getLevelAtStep(), user.getPlayingMap());
                return;
            } else {
                user.setMoveCount(0);
            }
        }

        showLevelMenu();
        primaryStage.setTitle("Sokoban");
        primaryStage.show();
    }

    public void showLevelMenu() {
        root.getChildren().clear();
        root.setLayoutX(0); root.setLayoutY(0);
        Pane level_menu_root = new Pane();
        root.getChildren().add(level_menu_root);
        level_menu = new SelectMap(primaryStage, level_menu_root, root);
        scene = primaryStage.getScene();
        scene.setRoot(root);
        level_menu.add_levels(maps, user);
        level_menu.update();
        InLevelMenu();
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public void save(String s) throws FileNotFoundException {
//        if(user.getLevelAtStep() > 0) {
            user.setPlayingMap(level.getMap().getMatrix());
            user.setLevelAt(currentLevel);
            user.setLevelAtStep(level.getStep());
            user.setMaxLevel(Math.max(currentLevel, user.getMaxLevel()));
//        }
        SavingManager.save();
        save_text(s);
    }
    public void save_text(String s) {
        Label label = new Label(s);
        label.setTextFill(Color.WHITE);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-background-color: #007BFF;");

        // 添加
        VBox layout = new VBox(label);
        root.getChildren().addLast(layout);

        // 淡入淡出
        FadeTransition fadeIn = new FadeTransition(Duration.millis(config.fade_anim_duration), label);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setOnFinished(event -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(config.fade_anim_duration), label);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setDelay(Duration.millis(config.save_text_maintain));
            fadeOut.setOnFinished(event2 -> root.getChildren().remove(layout));
            fadeOut.play();
        });

        fadeIn.play();

    }

}
