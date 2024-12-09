package org.view.menu;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.model.config;
import org.view.menu.MenuView;

import static org.view.menu.MenuView.mediaPlayer;

public class Settings {
    Button settingsButton;

    public Button createButton(Pane root) {
        settingsButton = new Button();
        ImageView settingsImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/settings.png")));
        settingsImageView.setFitWidth(30);
        settingsImageView.setFitHeight(30);
        settingsButton.setGraphic(settingsImageView);
        settingsButton.setMaxSize(30, 30);
        settingsButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px;");
        settingsButton.setLayoutX(root.getWidth() - 80);
        settingsButton.setLayoutY(35.0);
        settingsButton.setOnMouseClicked(event -> settingsButtonClicked(root));
        //鼠标放上去时变成 settings_clicked.png
        settingsButton.setOnMouseEntered(event -> {
            settingsImageView.setImage(new Image(getClass().getResourceAsStream("/images/settings_clicked.png")));
        });
        settingsButton.setOnMouseExited(event -> {
            settingsImageView.setImage(new Image(getClass().getResourceAsStream("/images/settings.png")));
        });
        //取消 settingsButton 对上下左右键的监听
        settingsButton.setFocusTraversable(false);
        //添加屏幕大小改变时的监听
        root.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(shade != null) shade.setWidth(newValue.doubleValue());
        });
        root.heightProperty().addListener((observable, oldValue, newValue) -> {
            if(shade != null) shade.setHeight(newValue.doubleValue());
        });

        return settingsButton;
    }
    Rectangle shade;
    ImageView paper = new ImageView(new Image(getClass().getResourceAsStream("/images/paper.png"), 500, 500, false, false));

    public VBox SettingVbox;
    public HBox AnimTimeHbox;
    public HBox VolumeHbox;
    public Text MovingAnimTimeText;
    public Slider MovingAnimTimeSlider;
    public Text VolumeText;
    public Slider VolumeSlider;
    public HBox IsVerticalHbox;
    public Text IsVerticalText;
    public CheckBox VerticalCheckBox;
    Font pixelFont = Font.loadFont(getClass().getResource("/font/pixel.ttf").toExternalForm(), 30);

    private void settingsButtonClicked(Pane root) {
        // Handle settings button click
//        menuController.SettingButtonClicked();

        //变暗
        shade = new Rectangle(0, 0, root.getWidth(), root.getHeight());
        shade.setFill(Color.rgb(0, 0, 0, 0.5));
        root.getChildren().add(shade);
        //纸
        paper.setX(150);
        paper.setY(50);
        root.getChildren().add(paper);
//        标题
        Text loginText = new Text(310.0, 150.0, "Settings");
        loginText.setFill(javafx.scene.paint.Color.web("#55371d"));
        loginText.setFont(new Font(pixelFont.getName(), 45));
        root.getChildren().add(loginText);

        SettingVbox = new VBox(0);
        SettingVbox.setPrefHeight(511.0);
        SettingVbox.setPrefWidth(340.0);
        SettingVbox.setLayoutX(240);
        SettingVbox.setLayoutY(170);
        root.getChildren().add(SettingVbox);

        // 创建动画时间HBox
        AnimTimeHbox = new HBox();
        AnimTimeHbox.setAlignment(Pos.CENTER_LEFT);
        AnimTimeHbox.setPrefHeight(100.0);
        AnimTimeHbox.setPrefWidth(200.0);
        AnimTimeHbox.setSpacing(30.0);
        SettingVbox.getChildren().add(AnimTimeHbox);

        // 创建移动动画时间文本Text
        MovingAnimTimeText = new Text("MovingAnimTime");
        MovingAnimTimeText.setFill(javafx.scene.paint.Color.web("#55371d"));
        MovingAnimTimeText.setFont(new Font(pixelFont.getName(), 25));
        MovingAnimTimeText.setStrokeType(StrokeType.OUTSIDE);
        MovingAnimTimeText.setStrokeWidth(0.0);
        AnimTimeHbox.getChildren().add(MovingAnimTimeText);

//        if(true) return;

        // 创建移动动画时间Slider
        if (MovingAnimTimeSlider == null) {
            MovingAnimTimeSlider = new Slider();
            MovingAnimTimeSlider.setMax(0.69);
            MovingAnimTimeSlider.setValue(0.5);
            //将初始化的值设置为config中的值
            MovingAnimTimeSlider.setValue((config.move_anim_duration - 10) / 600.0);
            //把滑动条变成棕色并把边框去掉
            MovingAnimTimeSlider.setStyle("-fx-border-color: transparent; -fx-border-width: 0px; -fx-control-inner-background: #55371d; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
            MovingAnimTimeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                config.move_anim_duration = (int) (10 + 600 * newValue.doubleValue());
            });
        }
        AnimTimeHbox.getChildren().add(MovingAnimTimeSlider);

        // 创建音量HBox
        VolumeHbox = new HBox();
        VolumeHbox.setAlignment(Pos.CENTER_LEFT);
        VolumeHbox.setPrefHeight(100.0);
        VolumeHbox.setPrefWidth(200.0);
        VolumeHbox.setSpacing(30.0);
        SettingVbox.getChildren().add(VolumeHbox);

        // 创建音量文本Text
        VolumeText = new Text("Volume        ");
        VolumeText.setFill(javafx.scene.paint.Color.web("#55371d"));
        VolumeText.setFont(new Font(pixelFont.getName(), 25));
        VolumeText.setStrokeType(StrokeType.OUTSIDE);
        VolumeText.setStrokeWidth(0.0);
        VolumeHbox.getChildren().add(VolumeText);

        // 创建音量Slider
        if (VolumeSlider == null) {
            VolumeSlider = new Slider();
            VolumeSlider.setMax(1);
            VolumeSlider.setValue(config.volume);
            // 把滑动条变成棕色并把边框去掉
            VolumeSlider.setStyle("-fx-border-color: transparent; -fx-border-width: 0px; -fx-control-inner-background: #55371d; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
            VolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                config.volume = newValue.doubleValue();
                mediaPlayer.setVolume(newValue.doubleValue());
            });
        }
        VolumeHbox.getChildren().add(VolumeSlider);

        // 创建是否垂直HBox
        IsVerticalHbox = new HBox();
        IsVerticalHbox.setAlignment(Pos.CENTER_LEFT);
        IsVerticalHbox.setPrefHeight(100.0);
        IsVerticalHbox.setPrefWidth(200.0);
        IsVerticalHbox.setSpacing(30.0);
        SettingVbox.getChildren().add(IsVerticalHbox);

        // 创建是否垂直文本Text
        IsVerticalText = new Text("显示地图的方向");
        IsVerticalText.setFill(javafx.scene.paint.Color.web("#55371d"));
        IsVerticalText.setFont(new Font(pixelFont.getName(), 20));
        IsVerticalText.setStrokeType(StrokeType.OUTSIDE);
        IsVerticalText.setStrokeWidth(0.0);
        IsVerticalHbox.getChildren().add(IsVerticalText);

        // 创建是否垂直CheckBox
        if (VerticalCheckBox == null) {
            VerticalCheckBox = new CheckBox("Vertical");
            //将文本改为棕色
            VerticalCheckBox.setTextFill(javafx.scene.paint.Color.web("#55371d"));
            VerticalCheckBox.setStyle("-fx-mark-color: #55371d; -fx-box-border: transparent; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
            VerticalCheckBox.setFont(new Font(pixelFont.getName(), 20));
            VerticalCheckBox.setMnemonicParsing(false);
        }
        IsVerticalHbox.getChildren().add(VerticalCheckBox);
        // onMouseClicked="#Clicked" 需要在控制器中处理


        //关闭按钮
        Button close = new Button();
        Image X = new Image(getClass().getResourceAsStream("/images/X.png"), 30, 30, false, false);
        close.setGraphic(new ImageView(X));
        root.getChildren().add(close);
        close.setLayoutX(630);
        close.setLayoutY(60);
        close.setMaxSize(30, 30);
        close.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px; -fx-text-fill: #55371d;");
        //点击关闭按钮
        close.setOnMouseClicked(event -> {
            root.getChildren().remove(shade);
            root.getChildren().remove(paper);
            root.getChildren().remove(loginText);
            root.getChildren().remove(SettingVbox);
            root.getChildren().remove(close);
        });
    }
}
