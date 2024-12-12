package org.view.menu;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.model.config;
import org.view.level.Grass;

import java.util.ArrayList;

public class Theme {

    Button themeButton;
    Pane root;
    public Button createButton(Pane root) {
        this.root = root;
        themeButton = new Button();
        ImageView themeImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/theme.png")));
        themeImageView.setFitWidth(30);
        themeImageView.setFitHeight(30);
        themeButton.setGraphic(themeImageView);
        themeButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px;");
        themeButton.setLayoutX(580.0);
        themeButton.setLayoutY(35.0);
        themeButton.setMaxSize(30, 30);
        themeButton.setOnMouseClicked(event -> {
            themeButtonClicked();
        });
        themeButton.setOnMouseEntered(event -> {
            themeImageView.setImage(new Image(getClass().getResourceAsStream("/images/theme_clicked.png")));
        });
        themeButton.setOnMouseExited(event -> {
            themeImageView.setImage(new Image(getClass().getResourceAsStream("/images/theme.png")));
        });
        //取消按钮对上下左右键的控制
        themeButton.setFocusTraversable(false);
        return themeButton;
    }
    Rectangle shade;
    ImageView paper = new ImageView(new Image(getClass().getResourceAsStream("/images/paper.png"), 500, 500, false, false));
    Font pixelFont = Font.loadFont(getClass().getResource("/font/pixel.ttf").toExternalForm(), 30);
    Button close;
    void themeButtonClicked(){
        //变暗
        shade = new Rectangle(0, 0, root.getWidth(), root.getHeight());
        shade.setFill(Color.rgb(0, 0, 0, 0.5));
        root.getChildren().add(shade);
        //纸
        paper.setX(150);
        paper.setY(50);
        root.getChildren().add(paper);

        //标题
        Text themeText = new Text(360, 150, "主题");
        themeText.setFont(new Font(pixelFont.getName(), 40));
        themeText.setFill(Color.web("#55371d"));
        root.getChildren().add(themeText);

        //主题
        ToggleGroup themeGroup = new ToggleGroup();

        RadioButton theme1 = new RadioButton("苔藓绿");
        //字体
        theme1.setFont(new Font(pixelFont.getName(), 20));
        //字体颜色为绿色
        theme1.setTextFill(Color.rgb(124, 153, 32));
        theme1.setLayoutX(350);
        theme1.setLayoutY(200);
        theme1.setToggleGroup(themeGroup);
        theme1.setOnMouseClicked(event ->{
            setThemeColor(Color.rgb(124, 153, 32));
        });

        RadioButton theme2 = new RadioButton("春梅红");
        //字体
        theme2.setFont(new Font(pixelFont.getName(), 20));
        //字体颜色为红色
        theme2.setTextFill(Color.rgb(241, 147, 156));
        theme2.setLayoutX(350);
        theme2.setLayoutY(250);
        theme2.setToggleGroup(themeGroup);
        theme2.setOnMouseClicked(event -> {
            setThemeColor(Color.rgb(241, 147, 156));
        });

        RadioButton theme3 = new RadioButton("远山紫");
        //字体
        theme3.setFont(new Font(pixelFont.getName(), 20));
        //字体颜色为紫色
        theme3.setTextFill(Color.rgb(204, 204, 214));
        theme3.setLayoutX(350);
        theme3.setLayoutY(300);
        theme3.setToggleGroup(themeGroup);
        theme3.setOnMouseClicked(event -> {
            setThemeColor(Color.rgb(204, 204, 214));
        });


        RadioButton theme4 = new RadioButton("深灰蓝");
        //字体
        theme4.setFont(new Font(pixelFont.getName(), 20));
        //字体颜色为蓝色
        theme4.setTextFill(Color.rgb(19, 44, 51));
        theme4.setLayoutX(350);
        theme4.setLayoutY(350);
        theme4.setToggleGroup(themeGroup);
        theme4.setOnMouseClicked(event -> {
            setThemeColor(Color.rgb(19, 44, 51));
        });


        RadioButton theme0 = new RadioButton("yym 色");
        //字体
        theme0.setFont(new Font(pixelFont.getName(), 20));
        //字体颜色
        theme0.setTextFill(Color.rgb(124, 113, 32));
        theme0.setLayoutX(350);
        theme0.setLayoutY(400);
        theme0.setToggleGroup(themeGroup);
        theme0.setOnMouseClicked(event -> {
            setThemeColor(Color.rgb(124, 113, 32));
        });

        RadioButton theme5 = new RadioButton("gyx 色");
        //字体
        theme5.setFont(new Font(pixelFont.getName(), 20));
        //字体颜色
        theme5.setTextFill(Color.rgb(124, 111, 52));
        theme5.setLayoutX(350);
        theme5.setLayoutY(450);
        theme5.setToggleGroup(themeGroup);
        theme5.setOnMouseClicked(event -> {
            setThemeColor(Color.rgb(124, 111, 52));
        });
        theme5.setOnMouseEntered(event -> {
            root.getChildren().remove(theme5); //gyx 已经被移除
        });

        root.getChildren().addAll(theme1, theme2, theme3, theme4, theme0, theme5);

        //关闭
        close = new Button();
        Image X = new Image(getClass().getResourceAsStream("/images/X.png"), 30, 30, false, false);
        close.setGraphic(new ImageView(X));
        root.getChildren().add(close);
        close.setLayoutX(630);
        close.setLayoutY(60);
        close.setMaxSize(30, 30);
        close.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0px; -fx-text-fill: #55371d;");
        //点击关闭按钮
        close.setOnMouseClicked(event -> {
            root.getChildren().removeAll(shade, paper, close,
                    theme1, theme2, theme3, theme4, theme0, theme5,
                    themeText);
        });

    }

    private void setThemeColor(Color color){
        config.themeColor = color;
        for(Rectangle rect : MenuView.grass){
            rect.setFill(Grass.randColor(MenuView.grassI.get(MenuView.grass.indexOf(rect)), MenuView.grassI.get(MenuView.grass.indexOf(rect))));
        }
    }
}
