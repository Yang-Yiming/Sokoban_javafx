package org.view.menu;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.model.SavingManager;

import java.awt.*;
import java.io.IOException;

public class LoginController {

    private Stage thisStage;
    private Stage MenuStage;

    private String UserName;
    private String Password;

    public LoginController() {
    }

    public void initialize(Stage LoginStage, Stage MenuStage) {
        this.thisStage = LoginStage;
        this.MenuStage = MenuStage;

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
    private TextField PasswordInput;

    @FXML
    private Text PasswordText;

    @FXML
    private HBox ConfirmPasswordHbox;

    @FXML
    private TextField ConfirmPasswordInput;

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

    @FXML
    void HandleLogin(MouseEvent event) {
        if(LoginVbox.getChildren().contains(ConfirmPasswordHbox)){
            LoginVbox.getChildren().remove(ConfirmPasswordHbox);
            UsernameInput.setText(""); PasswordInput.setText("");
            return;
        }

        UserName = UsernameInput.getText();
        Password = PasswordInput.getText();

        SavingManager.readUsersInfo();
        int userid = SavingManager.getUser(UserName, Password);

        if (userid == -1) {
            ReminderText.setText("User not found");
        } else if (userid == -2) {
            ReminderText.setText("Wrong password");
        } else {
            ReminderText.setText("Login successfully");
        }
        LoginVbox.getChildren().add(2, ReminderHbox);
    }

    @FXML
    void HandleRegister(MouseEvent event) {
        if(!LoginVbox.getChildren().contains(ConfirmPasswordHbox)){
            LoginVbox.getChildren().add(2, ConfirmPasswordHbox);
            return;
        }

        UserName = UsernameInput.getText();
        Password = PasswordInput.getText();
        String ConfirmPassword = ConfirmPasswordInput.getText();

        LoginVbox.getChildren().add(3, ReminderHbox);

        if (!SavingManager.validString(UserName)) {
            ReminderText.setText("Invalid username");
        } else if (SavingManager.getUser(UserName, Password) != -1) {
            ReminderText.setText("User already exists");
        } else if (!SavingManager.validString(Password)) {
            ReminderText.setText("Invalid password");
        } else if (!Password.equals(ConfirmPassword)) {
            ReminderText.setText("Password not match");
        } else {
            SavingManager.addUser(UserName, Password);
            ReminderText.setText("Register successfully");
            LoginVbox.getChildren().remove(ConfirmPasswordHbox);
        }
    }

    @FXML
    public void Back(MouseEvent mouseEvent) throws IOException {
        // 使用TranslateTransition实现向右侧滑出的动画效果
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), thisStage.getScene().getRoot());
        translateTransition.setFromX(0);
        translateTransition.setToX(thisStage.getWidth());

        translateTransition.setOnFinished(e -> {
            thisStage.close(); // 播放完后关闭窗口
        });

        // 让主屏幕变回来
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(MenuStage.getScene().getRoot().opacityProperty(), 1);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        translateTransition.play();
    }
}
