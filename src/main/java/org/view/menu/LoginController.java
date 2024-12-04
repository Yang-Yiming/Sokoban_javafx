package org.view.menu;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.model.SavingManager;
import org.model.User;

import java.io.FileNotFoundException;
import java.io.IOException;

public class LoginController extends HalfStageController{

    private String UserName;
    private String Password;

    private User user;
    private MenuController menu;

    public LoginController() {
    }

    public void initialize(Stage LoginStage, Stage MenuStage, MenuController menu) {
        this.thisStage = LoginStage;
        this.MenuStage = MenuStage;
        this.menu = menu;
        this.user = menu.get_user();

        LoginVbox.getChildren().remove(ReminderHbox); // 将提醒先删去
        LoginVbox.getChildren().remove(ConfirmPasswordHbox); // 将确认密码先删去
    }


    @FXML
    private Button Back;

    @FXML
    private javafx.scene.shape.Rectangle BackGround;

    @FXML
    private VBox LoginVbox;

    @FXML
    private HBox ButtonsHbox;

    @FXML
    private Button LoginButton;

    @FXML
    private HBox PasswordHbox;

    @FXML
    private PasswordField PasswordInput;

    @FXML
    private Text PasswordText;

    @FXML
    private HBox ConfirmPasswordHbox;

    @FXML
    private PasswordField ConfirmPasswordInput;

    @FXML
    private Text ConfirmPasswordText;

    @FXML
    private HBox ReminderHbox;

    @FXML
    private Text ReminderText;

    @FXML
    private Button RegisterButton;

    @FXML
    private HBox UsernameHbox;

    @FXML
    private TextField UsernameInput;

    @FXML
    private Text UsernameText;

//    @FXML

//    @FXML

    @FXML
    public void Back(MouseEvent mouseEvent) throws IOException {
        menu.set_user(user);
        back_to_menu();
    }
}
