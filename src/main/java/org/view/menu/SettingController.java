package org.view.menu;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.model.config;

import java.io.IOException;

public class SettingController extends HalfStageController{

    @FXML
    private HBox AnimTimeHbox;

    @FXML
    private Rectangle Background;

    @FXML
    private HBox IsVerticalHbox;

    @FXML
    private Text IsVerticalText;

    @FXML
    private Slider MovingAnimTimeSlider;

    @FXML
    private Text MovingAnimTimeText;

    @FXML
    private Slider ViewingAngleSlider;

    @FXML
    private Text ViewingAngleText;

    @FXML
    private Text SettingText;

    @FXML
    private HBox SettingTextHbox;

    @FXML
    private VBox SettingVbox;

    @FXML
    private CheckBox VerticalCheckBox;

    @FXML
    private Button BackButton;

    @FXML
    void Clicked(MouseEvent event) {
        config.is_vertical = VerticalCheckBox.isSelected();
    }

    @FXML
    void DragDone(DragEvent event) {
        config.move_anim_duration = (int) (10 + 600 * MovingAnimTimeSlider.getValue());
       // config.viewing_angle_amount = (int) (0.2 + 0.6 * ViewingAngleSlider.getValue());// 会出问题
    }

    @FXML
    public void Back(MouseEvent mouseEvent) throws IOException {
        DragDone(null);
        back_to_menu();
    }

}
