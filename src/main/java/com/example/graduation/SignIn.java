package com.example.graduation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class SignIn implements Initializable {
    @FXML
    private ImageView img_logo;
    @FXML
    private TextField tf_user;
    @FXML
    private PasswordField tf_lock;
    @FXML
    private Button button_login;
    @FXML
    private Button button_signup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_signup.setOnAction(event ->
            DBUtils.changeScene(event,
                    "sign-up.fxml",
                    "Sign Up",
                    null)
        );
        button_login.setOnAction(event -> DBUtils.logInUser(event, tf_user.getText(), tf_lock.getText()));
    }


}
