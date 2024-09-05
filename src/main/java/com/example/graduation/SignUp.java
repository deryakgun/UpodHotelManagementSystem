package com.example.graduation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUp implements Initializable {
    @FXML
    private Button button_signup;
    @FXML
    private  Button button_login;
    @FXML
    private TextField tf_user;
    @FXML
    private TextField tf_lock;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_signup.setOnAction(event ->{
            if (!tf_user.getText().trim().isEmpty() && !tf_lock.getText().trim().isEmpty()){
                DBUtils.signUpUser(event, tf_user.getText(), tf_lock.getText());
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill all information to sign up!");
                alert.show();
            }
        });

        button_login.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in!", null));
    }
}
