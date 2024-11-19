package org.view.menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private Stage primaryStage;

    private String UserName;
    private String Password;

    public LoginController() {
    }

    public void initialize(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    private Button Back;

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
    private Button RegisterButton;

    @FXML
    private HBox UsernameHbox;

    @FXML
    private TextField UsernameInput;

    @FXML
    private Text UsernameText;

    @FXML
    void HandleLogin(MouseEvent event) {
        UserName = UsernameInput.getText();
        Password = PasswordInput.getText();
    }

    @FXML
    void HandleRegister(MouseEvent event) {

    }

    @FXML
    public void Back(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Menu.fxml"));
        Pane menuroot = loader.load();
        primaryStage.setScene(new Scene(menuroot));
        primaryStage.show();
    }
}
